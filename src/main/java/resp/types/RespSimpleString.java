package resp.types;

/**
 * Represents a RESP Simple String type.
 * This record encapsulates a simple string message in the RESP protocol.
 */

public record RespSimpleString(String message) implements RespType {
    public RespSimpleString {
        if (message == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
    }
}
