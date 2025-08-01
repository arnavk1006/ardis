package resp.parser.impl;

import org.junit.jupiter.api.Test;
import resp.parser.RespParser;
import resp.types.RespInteger;
import resp.types.RespType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ParseIntegerTest {
    @Test
    void testParsePositiveInteger() throws IOException {
        String respData = ":+12345\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespInteger integer = assertInstanceOf(RespInteger.class, result);
        assertEquals(12345, integer.value());
    }

    @Test
    void testParseNegativeInteger() throws IOException {
        String respData = ":-12345\r\n";
        InputStream inputStream = new ByteArrayInputStream(respData.getBytes(StandardCharsets.UTF_8));

        RespType result = RespParser.parse(inputStream);

        RespInteger integer = assertInstanceOf(RespInteger.class, result);
        assertEquals(-12345, integer.value());
    }
}
