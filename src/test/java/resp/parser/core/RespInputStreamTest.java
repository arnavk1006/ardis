package resp.parser.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


class RespInputStreamTest {
    private RespInputStream createStream(String input) {
        return new RespInputStream(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    private RespInputStream createStream(byte[] input) {
        return new RespInputStream(new ByteArrayInputStream(input));
    }

    @Test
    void readLineBasic() throws IOException {
        RespInputStream respStream = createStream("hello\r\n");
        assertEquals("hello", respStream.readLine());
    }

    @Test
    void readEmptyLine() throws IOException {
        RespInputStream respStream = createStream("\r\n");
        assertEquals("", respStream.readLine());
    }

    @Test
    void readLineMultipleLines() throws IOException {
        RespInputStream respStream = createStream("hello\r\nworld\r\n!");
        assertEquals("hello", respStream.readLine());
        assertEquals("world", respStream.readLine());
        assertThrows(IOException.class, respStream::readLine);
    }

    @Test
    void readLineWithNonStandardEnding() {
        RespInputStream respStream = createStream("hello\rworld\r\n");
        assertThrows(IOException.class, respStream::readLine);
    }

    @Test
    void readBytesBasic() throws IOException {
        byte[] testData = {'h', 'e', 'l', 'l', 'o', '\r'};
        RespInputStream respStream = createStream(testData);
        assertArrayEquals(testData, respStream.readBytes(6));
    }

    @Test
    void readNullByte() throws IOException {
        byte[] testData = {'\0'};
        RespInputStream respStream = createStream(testData);
        assertArrayEquals(testData, respStream.readBytes(1));
    }

    @Test
    void readZeroBytes() throws IOException {
        RespInputStream respStream = createStream("hello");
        assertEquals(0, respStream.readBytes(0).length);
    }

    @Test
    void readMultipleBytes() throws IOException {
        byte[] testData = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd'};
        RespInputStream respStream = createStream(testData);
        assertArrayEquals(new byte[]{'h', 'e', 'l', 'l', 'o'}, respStream.readBytes(5));
        assertArrayEquals(new byte[]{'w', 'o', 'r', 'l', 'd'}, respStream.readBytes(5));
    }

    @Test
    void readBytesExceedingInput() {
        byte[] testData = {'\0'};
        RespInputStream respStream = createStream(testData);
        assertThrows(IOException.class, () -> respStream.readBytes(2));
    }

    @Test
    void readTypeEmptyStream() {
        RespInputStream respStream = createStream("");
        assertThrows(IOException.class, respStream::readType);
    }
}
