package resp.parser.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.nio.charset.StandardCharsets;

import resp.types.*;

public class RespParserTest {
    @Test
    void testParseSimpleString() throws IOException {
        String respData = "+OK\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespSimpleString simpleString = assertInstanceOf(RespSimpleString.class, result);
        assertEquals("OK", simpleString.message());
    }

    @Test
    void testParseSimpleError() throws IOException {
        String respData = "-Error message\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespSimpleError simpleError = assertInstanceOf(RespSimpleError.class, result);
        // TODO: Add a test for an error type later on when it is implemented
        assertEquals("Error message", simpleError.message());
    }

    @Test
    void testParseInteger() throws IOException {
        String respData = ":12345\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespInteger integer = assertInstanceOf(RespInteger.class, result);
        assertEquals(12345, integer.value());
    }

    @Test
    void testParseBulkString() throws IOException, IllegalArgumentException {
        String respData = "$6\r\nfoobar\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespBulkString bulkString = assertInstanceOf(RespBulkString.class, result);
        assertArrayEquals("foobar".getBytes(StandardCharsets.UTF_8), bulkString.value());
        assertEquals("foobar", bulkString.toString());
        assertFalse(bulkString.isNull());
    }

    @Test
    void testParseArray() throws IOException, IndexOutOfBoundsException, NullPointerException {
        String respData = "*3\r\n$5\r\nhello\r\n$5\r\nworld\r\n:3\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespArray array = assertInstanceOf(RespArray.class, result);
        assertEquals(3, array.getLength());
        assertFalse(array.isNull());
        assertFalse(array.isEmpty());

        assertEquals("hello", ((RespBulkString) array.getIndex(0)).toString());
        assertEquals("world", ((RespBulkString) array.getIndex(1)).toString());
        assertEquals(3, ((RespInteger) array.getIndex(2)).value());
    }

    @Test
    void testUnrecognizedType() {
        String respData = "X\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        assertThrows(IOException.class, () -> RespParser.parse(inputStream));
    }
}
