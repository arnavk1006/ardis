package resp.parser.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A specialized input stream reader for RESP protocol parsing.
 * Provides methods to read RESP-compliant lines (ending with \r\n)
 * and exact byte sequences from an underlying InputStream.
 */
public class RespInputStream {
    private final InputStream inputStream;

    /**
     * Creates a new {@code RespInputStream} wrapping the provided InputStream using
     * the adapter pattern.
     *
     * @param inputStream the underlying input stream to read from
     * @throws IllegalArgumentException if inputStream is null
     */
    public RespInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        this.inputStream = inputStream;
    }

    /**
     * Reads a single line from the input stream, expecting RESP2-compliant line endings.
     * A valid line must end with the exact sequence \r\n (carriage return followed by
     * new line). The returned string does not include the \r\n terminator.
     *
     * @return the line content as a UTF-8 string, without the \r\n terminator
     * @throws IOException if the stream ends before finding a complete \r\n sequence
     * @throws IOException if a \r character is followed by anything other than \n
     * @throws IOException if the stream ends without any \r\n terminator
     */
    public String readLine() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int byteRead;
        boolean completedLine = false;
        while((byteRead = inputStream.read()) != -1) {
            if(byteRead == '\r') {
                int nextByte = inputStream.read();
                if (nextByte == '\n') {
                    completedLine = true;
                    break;
                } else if (nextByte == -1) {
                    throw new IOException("Unexpected end of stream while reading line");
                } else {
                    throw new IOException("Unexpected character '" + (char)nextByte + "' after '\\r' in line");
                }
            } else {
                buffer.write(byteRead);
            }
        }

        if(!completedLine) {
            throw new IOException("Line did not end with '\\r\\n'");
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    /**
     * Reads exactly the specified number of bytes from the input stream.
     * This method will block until all requested bytes are available or
     * the stream reaches an end-of-file.
     *
     * @param numBytes the exact number of bytes to read. Must be >= 0
     * @return a byte array containing exactly {@code numBytes} bytes
     * @throws IOException if the stream ends before {@code numBytes} can be read
     * @throws IllegalArgumentException if {@code numBytes} is negative
     */
    public byte[] readBytes(int numBytes) throws IOException {
        if (numBytes < 0) {
            throw new IllegalArgumentException("Number of bytes cannot be negative");
        }
        if (numBytes == 0) {
            return new byte[0];
        }

        byte[] buffer = new byte[numBytes];
        int totalBytesRead = 0;

        while (totalBytesRead < numBytes) {
            int bytesRead = inputStream.read(buffer, totalBytesRead, numBytes - totalBytesRead);
            if (bytesRead == -1) {
                throw new IOException("Unexpected end of stream while reading " + numBytes + " bytes");
            }
            totalBytesRead += bytesRead;
        }

        return buffer;
    }

    /**
     * Reads a single byte from the input stream, which is expected to be a RESP type indicator.
     * This method is used to determine the type of RESP data that follows. It is a convenience method
     * for the parser dispatch logic.
     *
     * @return the byte indicating the RESP type (e.g., '+', '-', ':', '$', '*')
     * @throws IOException if the stream ends before reading a byte
     */
    public byte readType() throws IOException {
        return readBytes(1)[0];
    }
}
