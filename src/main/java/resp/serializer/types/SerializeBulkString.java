package resp.serializer.types;

import resp.serializer.core.SerializerType;
import resp.types.RespBulkString;
import resp.types.RespType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerializeBulkString implements SerializerType {
    private static final byte PREFIX = '$';

    @Override
    public void serialize(RespType respType, OutputStream outputStream) throws IllegalArgumentException, IOException {
        if(!(respType instanceof RespBulkString respBulkString)) {
            throw new IllegalArgumentException("Expected RespBulkString type for serialization, but got: " + respType.getClass().getName());
        }

        final byte[] value = respBulkString.value();

        outputStream.write(PREFIX);

        if (value == null) {
            outputStream.write("-1".getBytes(StandardCharsets.UTF_8));
        } else {
            outputStream.write(String.valueOf(value.length).getBytes(StandardCharsets.UTF_8));
            outputStream.write(CRLF);
            outputStream.write(value);
        }

        outputStream.write(CRLF);
    }
}
