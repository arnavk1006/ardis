package resp.types;

/**
 * Interface representing a RESP type.
 * This interface serves as a marker for all RESP types in the RESP protocol.
 * It is used to ensure that all RESP types implement this interface, allowing
 * for polymorphism and type checking in the RESP parser and handler.
 */

public interface RespType {}
