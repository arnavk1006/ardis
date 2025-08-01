package resp.serializer.impl;

import org.junit.jupiter.api.Test;
import resp.serializer.RespSerializer;
import resp.types.RespBulkString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializeBulkStringTest {
    @Test
    void serializeBulkStringEmpty() throws IllegalArgumentException, IOException {
        RespBulkString bulkString = new RespBulkString(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(bulkString, outputStream);
        String result = outputStream.toString();
        assertEquals("$0\r\n\r\n", result);
    }

    @Test
    void serializeBulkStringNull() throws IllegalArgumentException, IOException {
        RespBulkString bulkString = new RespBulkString(null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(bulkString, outputStream);
        String result = outputStream.toString();
        assertEquals("$-1\r\n", result);
    }

    @Test
    void serializeBulkStringSpecialCharacters() throws IllegalArgumentException, IOException {
        String data = "hello world!\n123";
        RespBulkString bulkString = new RespBulkString("hello world!\n123".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(bulkString, outputStream);
        String result = outputStream.toString();
        assertEquals("$" + data.getBytes(StandardCharsets.UTF_8).length + "\r\n" + data + "\r\n", result);
    }
}
