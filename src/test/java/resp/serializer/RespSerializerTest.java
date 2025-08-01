package resp.serializer;

import org.junit.jupiter.api.Test;
import resp.types.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class RespSerializerTest {
    @Test
    void testSerializeSimpleString() throws IllegalArgumentException, IOException {
        RespSimpleString simpleString = new RespSimpleString("hello world!");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(simpleString, outputStream);
        String result = outputStream.toString();
        assertEquals("+hello world!\r\n", result);
    }

    @Test
    void testSerializeSimpleError() throws IllegalArgumentException, IOException {
        RespSimpleError simpleError = new RespSimpleError("error message");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(simpleError, outputStream);
        String result = outputStream.toString();
        assertEquals("-error message\r\n", result);
    }

    @Test
    void testSerializeInteger() throws IllegalArgumentException, IOException {
        RespInteger integer = new RespInteger(12345);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(integer, outputStream);
        String result = outputStream.toString();
        assertEquals(":12345\r\n", result);
    }

    @Test
    void testSerializeBulkString() throws IllegalArgumentException, IOException {
        byte[] data = "foobar".getBytes(StandardCharsets.UTF_8);
        RespBulkString bulkString = new RespBulkString(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(bulkString, outputStream);
        String result = outputStream.toString();
        assertEquals("$6\r\nfoobar\r\n", result);
    }

    @Test
    void testSerializeArray() throws IllegalArgumentException, IOException {
        RespArray array = new RespArray(new RespType[]{
            new RespSimpleString("item1"),
            new RespInteger(42),
            new RespBulkString("item3".getBytes(StandardCharsets.UTF_8))
        });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(array, outputStream);
        String result = outputStream.toString();
        assertEquals("*3\r\n+item1\r\n:42\r\n$5\r\nitem3\r\n", result);
    }
}
