package resp.serializer.core;

import resp.types.RespType;

import java.io.IOException;
import java.io.OutputStream;

public interface SerializerType {
    byte[] CRLF = new byte[]{'\r', '\n'};

    void serialize(RespType respType, OutputStream outputStream) throws IllegalArgumentException, IOException;
}
