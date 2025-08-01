package resp.serializer.types;

import org.junit.jupiter.api.Test;
import resp.serializer.core.RespSerializer;
import resp.types.RespInteger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializeIntegerTest {
    @Test
    void testSerializeNegativeInteger() throws IllegalArgumentException, IOException {
        RespInteger integer = new RespInteger(-12345);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(integer, outputStream);

        String result = outputStream.toString();
        assertEquals(":-12345\r\n", result);
    }
}
