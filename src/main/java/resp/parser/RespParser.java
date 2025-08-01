package resp.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import resp.parser.impl.*;
import resp.types.RespType;

/**
 * A utility class for parsing RESP (Redis Serialization Protocol) messages.
 * This parser implements a dispatcher pattern to route different RESP data types
 * to their appropriate specialized parsers based on the first byte of the message.
 * The class is designed as a utility class with only static methods.
 * Currently only supports RESP2 protocol parsing.
 */
public class RespParser {

    /**
     * Mapping of RESP type indicator bytes to their corresponding parser implementations.
     * This map is populated statically and remains immutable.
     */
    private static final Map<Byte, ParserType> DISPATCH_MAP = Map.of(
        (byte) '+', new ParseSimpleString(),
        (byte) '-', new ParseSimpleError(),
        (byte) ':', new ParseInteger(),
        (byte) '$', new ParseBulkString(),
        (byte) '*', new ParseArray()
    );

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Since this class only contains static methods, it should not be instantiated
     * according to best practices for utility classes.
     *
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    private RespParser() {
        throw new UnsupportedOperationException("RespParser is a utility class and cannot be instantiated");
    }

    /**
     * Parses a RESP message from the provided input stream and returns the appropriate {@link RespType}.
     * This method reads the first byte from the input stream to determine the RESP data type,
     * then dispatches to the appropriate specialized parser to handle the remaining data.
     *
     * @param respInputStream the input stream containing the RESP message to parse.
     *                   Must not be null and should contain a valid RESP message starting
     *                   with a recognized type indicator byte.
     * @return a {@link RespType} object representing the parsed RESP data. The specific subtype
     *         depends on the type indicator found in the input stream.
     * @throws IOException according to the conditions defined in the {@link RespInputStream} class' {@code parse()}
     *                     method.
     * @throws IllegalArgumentException if inputStream is null (thrown by RespInputStream constructor)
     */
    public static RespType parse(RespInputStream respInputStream) throws IOException {
        byte type = respInputStream.readType();

        if (!DISPATCH_MAP.containsKey(type)) {
            // Bytes can be non-printable characters like control characters (e.g., null bytes,
            // bell characters, etc.). Convert them to their hexadecimal representation for
            // clear error reporting and debugging. The & 0xFF mask ensures the byte is treated
            // as unsigned when converting to hex.
            throw new IOException("Unknown RESP type: 0x" + Integer.toHexString(type & 0xFF));
        }

        ParserType parser = DISPATCH_MAP.get(type);
        return parser.parse(respInputStream);
    }

    /**
     * Convenience method for parsing a RESP message from a standard {@link InputStream}.
     * This overload wraps the provided InputStream in a {@link RespInputStream}, which
     * provides RESP-specific read operations, and then delegates parsing to the main
     * {@code parse(RespInputStream)} method.
     *
     * @param inputStream the raw InputStream containing the RESP message.
     *                    Must not be null and should contain a valid RESP message.
     * @return a {@link RespType} object representing the parsed RESP data.
     * @throws IOException              if an I/O error occurs during reading.
     * @throws IllegalArgumentException if inputStream is null.
     */
    public static RespType parse(InputStream inputStream) throws IOException, IllegalArgumentException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        RespInputStream respInputStream = new RespInputStream(inputStream);
        return parse(respInputStream);
    }
}
