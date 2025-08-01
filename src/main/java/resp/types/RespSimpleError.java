package resp.types;

/**
 * Represents a RESP Simple Error type.
 * This record encapsulates an error type and a message in the RESP protocol.
 * It is used to represent errors that occur during RESP command processing.
 * As specified in ErrorType, this implementation tries to enforce the
 * standard convention of using the first word of the message as the error type.
 */

public record RespSimpleError(String message) implements RespType {
    public RespSimpleError {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
    }
}
