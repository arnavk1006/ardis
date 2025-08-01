package resp.parser.types;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import resp.parser.core.RespParser;
import resp.types.*;

public class ParseArrayTest {
    @Test
    void testParseEmptyArray() throws IOException, IndexOutOfBoundsException, NullPointerException {
        String respData = "*0\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespArray array = assertInstanceOf(RespArray.class, result);
        assertEquals(0, array.getLength());
        assertTrue(array.isEmpty());
        assertFalse(array.isNull());

        assertThrows(IndexOutOfBoundsException.class, () -> array.getIndex(0));
    }

    @Test
    void testParseArrayExtraElement() throws IOException, IndexOutOfBoundsException, NullPointerException {
        String respData = "*2\r\n:1\r\n:2\r\n:3\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespArray array = assertInstanceOf(RespArray.class, result);
        assertEquals(2, array.getLength());
        assertFalse(array.isNull());
        assertFalse(array.isEmpty());

        RespInteger firstElement = assertInstanceOf(RespInteger.class, array.getIndex(0));
        assertEquals(1, firstElement.value());

        RespInteger secondElement = assertInstanceOf(RespInteger.class, array.getIndex(1));
        assertEquals(2, secondElement.value());

        assertEquals(":3\r\n".getBytes(StandardCharsets.UTF_8).length, inputStream.available());

        RespType remaining = RespParser.parse(inputStream);
        RespInteger remainingInteger = assertInstanceOf(RespInteger.class, remaining);
        assertEquals(3, remainingInteger.value());
    }

    @Test
    void testParseNestedArray() throws IOException, IndexOutOfBoundsException, NullPointerException {
        String respData = "*2\r\n*3\r\n:1\r\n:2\r\n:3\r\n*2\r\n+Hello\r\n-World\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespArray outerArray = assertInstanceOf(RespArray.class, result);
        assertEquals(2, outerArray.getLength());
        assertFalse(outerArray.isNull());
        assertFalse(outerArray.isEmpty());

        RespArray innerArray1 = assertInstanceOf(RespArray.class, outerArray.getIndex(0));
        assertEquals(3, innerArray1.getLength());
        assertFalse(innerArray1.isNull());
        assertFalse(innerArray1.isEmpty());

        assertEquals(1, ((RespInteger) innerArray1.getIndex(0)).value());
        assertEquals(2, ((RespInteger) innerArray1.getIndex(1)).value());
        assertEquals(3, ((RespInteger) innerArray1.getIndex(2)).value());

        RespArray innerArray2 = assertInstanceOf(RespArray.class, outerArray.getIndex(1));
        assertEquals(2, innerArray2.getLength());
        assertFalse(innerArray2.isNull());
        assertFalse(innerArray2.isEmpty());

        assertEquals("Hello", ((RespSimpleString) innerArray2.getIndex(0)).message());
        assertEquals("World", ((RespSimpleError) innerArray2.getIndex(1)).message());
    }

    @Test
    void testParseNullArray() throws IOException, IndexOutOfBoundsException, NullPointerException {
        String respData = "*-1\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespArray array = assertInstanceOf(RespArray.class, result);
        assertTrue(array.isNull());
        assertNull(array.elements());
        assertFalse(array.isEmpty());
        assertThrows(NullPointerException.class, array::getLength);
        assertThrows(NullPointerException.class, () -> array.getIndex(0));
    }

    @Test
    void testParseArrayWithNullElement() throws IOException, IndexOutOfBoundsException, NullPointerException {
        String respData = "*3\r\n$5\r\nhello\r\n$-1\r\n$5\r\nworld\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespArray array = assertInstanceOf(RespArray.class, result);
        assertEquals(3, array.getLength());
        assertFalse(array.isNull());
        assertFalse(array.isEmpty());

        assertEquals("hello", ((RespBulkString) array.getIndex(0)).toString());

        RespBulkString nullElement = assertInstanceOf(RespBulkString.class, array.getIndex(1));
        assertNull(nullElement.value());
        assertNull(nullElement.toString());

        assertEquals("world", ((RespBulkString) array.getIndex(2)).toString());
    }
}
