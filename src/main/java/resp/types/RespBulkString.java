package resp.types;

import java.nio.charset.StandardCharsets;

/**
 * Represents a RESP Bulk String type.
 * This record encapsulates a byte array that represents a bulk string in the RESP protocol.
 * The message is stored as a byte array, and it can be converted to a String using UTF-8 encoding
 * with the help of a convenience method.
 * Uses clones to prevent external modification of the array.
 */

public record RespBulkString(byte[] value) implements RespType {
    public RespBulkString(byte[] value) {
        this.value = (value == null) ? null : value.clone();
    }

    public boolean isNull() {
        return value == null;
    }

    @Override
    public byte[] value() {
        return this.isNull() ? null : value.clone();
    }

    @Override
    public String toString() {
        return this.isNull() ? null : new String(value, StandardCharsets.UTF_8);
    }
}
