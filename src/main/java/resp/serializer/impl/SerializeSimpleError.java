package resp.serializer.impl;

import resp.serializer.SerializerType;
import resp.types.RespType;
import resp.types.RespSimpleError;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerializeSimpleError implements SerializerType {
    private static final byte PREFIX = '-';

    @Override
    public void serialize(RespType respType, OutputStream outputStream) throws IllegalArgumentException, IOException {
        if(!(respType instanceof RespSimpleError(String error))) {
            throw new IllegalArgumentException("Expected RespSimpleError type for serialization, but got: " + respType.getClass().getName());
        }
        outputStream.write(PREFIX);
        if(error == null) {
            throw new IllegalArgumentException("RespSimpleError value cannot be null");
        }
        outputStream.write(error.getBytes(StandardCharsets.UTF_8));
        outputStream.write(CRLF);
    }
}
