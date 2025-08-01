package resp.types;

/**
 * Represents a RESP integer type.
 * This record encapsulates a long message that represents an integer in the RESP protocol.
 * Long is chosen because the protocol specifies that the returned integer is "guaranteed to be in the
 * range of a signed 64-bit integer"
 */

public record RespInteger(long value) implements RespType {}
