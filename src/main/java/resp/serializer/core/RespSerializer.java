package resp.serializer.core;

import resp.serializer.types.*;
import resp.types.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class RespSerializer {
    private static final Map<Class<? extends RespType>, SerializerType> DISPATCH_MAP = Map.of(
        RespArray.class, new SerializeArray(),
        RespBulkString.class, new SerializeBulkString(),
        RespInteger.class, new SerializeInteger(),
        RespSimpleString.class, new SerializeSimpleString(),
        RespSimpleError.class, new SerializeSimpleError()
    );

    private RespSerializer() {
        throw new UnsupportedOperationException("RespSerializer is a utility class and cannot be instantiated");
    }

    public static void serialize(RespType respType, OutputStream outputStream) throws IllegalArgumentException, IOException {
        if(respType == null) {
            throw new IllegalArgumentException("respType cannot be null");
        } else if(!DISPATCH_MAP.containsKey(respType.getClass())) {
            throw new IllegalArgumentException("Unsupported RespType: " + respType.getClass().getName());
        }
        SerializerType serializer = DISPATCH_MAP.get(respType.getClass());
        serializer.serialize(respType, outputStream);
    }
}
