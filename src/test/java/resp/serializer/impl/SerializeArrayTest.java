package resp.serializer.impl;

import org.junit.jupiter.api.Test;
import resp.serializer.RespSerializer;
import resp.types.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializeArrayTest {
    @Test
    void testSerializeEmptyArray() throws IllegalArgumentException, IOException {
        RespArray array = new RespArray(new RespType[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(array, outputStream);

        assertEquals("*0\r\n", outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void testSerializeNullArray() throws IllegalArgumentException, IOException {
        RespArray array = new RespArray(null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(array, outputStream);

        assertEquals("*-1\r\n", outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void testSerializeNestedArray() throws IllegalArgumentException, IOException {
        RespArray innerArray1 = new RespArray(new RespType[] {
                new RespSimpleString("item1"),
                new RespBulkString("item2".getBytes(StandardCharsets.UTF_8)),
                new RespInteger(34),
        });

        ByteArrayOutputStream innerArray1OutputStream = new ByteArrayOutputStream();
        RespSerializer.serialize(innerArray1, innerArray1OutputStream);
        String innerArray1Expected = "*3\r\n+item1\r\n$5\r\nitem2\r\n:34\r\n";
        assertEquals(innerArray1Expected, innerArray1OutputStream.toString(StandardCharsets.UTF_8));

        RespArray innerArray2 = new RespArray(new RespType[] {
                new RespSimpleString("item3"),
                new RespInteger(56),
                new RespBulkString("item4".getBytes(StandardCharsets.UTF_8)),
        });

        ByteArrayOutputStream innerArray2OutputStream = new ByteArrayOutputStream();
        RespSerializer.serialize(innerArray2, innerArray2OutputStream);
        String innerArray2Expected = "*3\r\n+item3\r\n:56\r\n$5\r\nitem4\r\n";
        assertEquals(innerArray2Expected, innerArray2OutputStream.toString(StandardCharsets.UTF_8));

        RespArray outerArray = new RespArray(new RespType[] {
                innerArray1,
                innerArray2,
        });

        ByteArrayOutputStream outerArrayOutputStream = new ByteArrayOutputStream();
        RespSerializer.serialize(outerArray, outerArrayOutputStream);
        String outerArrayExpected = "*2\r\n" + innerArray1Expected + innerArray2Expected;
        assertEquals(outerArrayExpected, outerArrayOutputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void testSerializeArrayWithNullElement() throws IllegalArgumentException, IOException {
        RespArray array = new RespArray(new RespType[] {
            new RespSimpleString("item1"),
            new RespBulkString(null),
            new RespArray(null),
            new RespInteger(42)
        });
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        RespSerializer.serialize(array, outputStream);

        String expected = "*4\r\n+item1\r\n$-1\r\n*-1\r\n:42\r\n";
        assertEquals(expected, outputStream.toString(StandardCharsets.UTF_8));
    }
}
