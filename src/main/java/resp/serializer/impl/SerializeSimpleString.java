package resp.serializer.impl;

import resp.serializer.SerializerType;
import resp.types.RespType;
import resp.types.RespSimpleString;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerializeSimpleString implements SerializerType {
    private static final byte PREFIX = '+';

    @Override
    public void serialize(RespType respType, OutputStream outputStream) throws IllegalArgumentException, IOException {
        if(!(respType instanceof RespSimpleString(String message))) {
            throw new IllegalArgumentException("Expected RespSimpleString type for serialization, but got: " + respType.getClass().getName());
        }
        outputStream.write(PREFIX);
        if (message == null) {
            throw new IllegalArgumentException("RespSimpleString value cannot be null");
        }
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.write(CRLF);
    }
}
