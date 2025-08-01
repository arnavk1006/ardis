package resp.parser.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import resp.parser.RespParser;
import resp.types.*;

public class ParseBulkStringTest {
    @Test
    void testParseBulkStringEmpty() throws IOException, IllegalArgumentException {
        String respData = "$0\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespBulkString bulkString = assertInstanceOf(RespBulkString.class, result);
        assertArrayEquals(new byte[0], bulkString.value());
        assertEquals("", bulkString.toString());
        assertFalse(bulkString.isNull());
    }

    @Test
    void testParseBulkStringNull() throws IOException, IllegalArgumentException {
        String respData = "$-1\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));
        RespType result = RespParser.parse(inputStream);

        RespBulkString bulkString = assertInstanceOf(RespBulkString.class, result);
        assertNull(bulkString.value());
        assertNull(bulkString.toString());
        assertTrue(bulkString.isNull());
    }

    @Test
    void testParseBulkStringExtra() throws IllegalArgumentException {
        String respData = "$5\r\nfoobar\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        assertThrows(IOException.class, () -> RespParser.parse(inputStream));
    }

    @Test
    void testParseBulkStringInvalidLength() throws IllegalArgumentException {
        String respData = "$-5\r\nfoobar\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> RespParser.parse(inputStream));
    }

    @Test
    void testParseBulkStringSpaceSpecialCharacters() throws IOException, IllegalArgumentException {
        String data = "hello world!\n123";
        String respData = "$" + data.getBytes(StandardCharsets.UTF_8).length + "\r\n" + data + "\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespBulkString bulkString = assertInstanceOf(RespBulkString.class, result);
        assertArrayEquals(data.getBytes(StandardCharsets.UTF_8), bulkString.value());
        assertEquals(data, bulkString.toString());
        assertFalse(bulkString.isNull());
    }
}
