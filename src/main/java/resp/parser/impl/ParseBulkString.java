package resp.parser.impl;

import resp.parser.ParserType;
import resp.parser.RespInputStream;
import resp.types.RespBulkString;
import resp.types.RespType;
import java.io.IOException;

/**
 * Parser for RESP Bulk Strings (identified by '$').
 * Bulk Strings contain binary-safe data with a prefixed length, followed by CRLF and optional payload.
 * They can also be used to represent nil values when the length is -1, similar to Arrays.
 */
public class ParseBulkString implements ParserType {

    /**
     * Parses a RESP Bulk String from the input stream.
     *
     * @param respInputStream the RESP input stream
     * @return a {@link RespBulkString}, or null if the bulk string is nil
     * @throws IOException potentially, because of the {@link RespInputStream} methods used,
     * as well as if the expected CRLF is not found after the bulk string.
     * @throws IllegalArgumentException if length is invalid
     */
    @Override
    public RespType parse(RespInputStream respInputStream) throws IOException {
        int length = Integer.parseInt(respInputStream.readLine());

        if (length == -1) {
            return new RespBulkString(null); // Null bulk string
        } else if (length < 0) {
            throw new IllegalArgumentException("Invalid bulk string length: " + length +
                    ". Length must be -1 or a non-negative integer.");
        }

        RespBulkString bulkString = new RespBulkString(respInputStream.readBytes(length));

        String remainingLine = respInputStream.readLine();
        if (!remainingLine.isEmpty()) {
            throw new IOException("Expected CRLF after bulk string, but found: " + remainingLine);
        }

        return bulkString;
    }
}
