package resp.serializer.types;

import resp.serializer.core.SerializerType;
import resp.types.RespInteger;
import resp.types.RespType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerializeInteger implements SerializerType {
    private static final byte PREFIX = ':';

    @Override
    public void serialize(RespType respType, OutputStream outputStream) throws IllegalArgumentException, IOException {
        if(!(respType instanceof RespInteger(long value))) {
            throw new IllegalArgumentException("Expected RespInteger type for serialization, but got: " + respType.getClass().getName());
        }
        outputStream.write(PREFIX);
        // RESP uses ascii for non-string types, so we convert the long value to a string in ASCII
        outputStream.write(String.valueOf(value).getBytes(StandardCharsets.UTF_8));
        outputStream.write(CRLF);
    }
}
