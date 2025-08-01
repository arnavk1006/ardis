package resp.serializer.types;

import resp.serializer.core.RespSerializer;
import resp.serializer.core.SerializerType;
import resp.types.RespArray;
import resp.types.RespType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerializeArray implements SerializerType {
    private static final byte PREFIX = '*';

    @Override
    public void serialize(RespType respType, OutputStream outputStream) throws IllegalArgumentException, IOException {
        if(!(respType instanceof RespArray respArray)) {
            throw new IllegalArgumentException("Expected RespArray type for serialization, but got: " + respType.getClass().getName());
        }

        final RespType[] elements = respArray.elements();

        outputStream.write(PREFIX);

        if(elements == null) {
            outputStream.write("-1".getBytes(StandardCharsets.UTF_8));
            outputStream.write(CRLF);
        } else {
            int length = elements.length;
            outputStream.write(String.valueOf(length).getBytes(StandardCharsets.UTF_8));
            outputStream.write(CRLF);
            for (RespType element : elements) {
                RespSerializer.serialize(element, outputStream);
            }
        }
    }
}
