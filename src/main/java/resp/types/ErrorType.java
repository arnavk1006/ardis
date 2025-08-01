package resp.types;

/**
 * Represents an error type in the RESP protocol.
 * This interface is used to define error types that can be returned by RESP commands.
 * Note: In the RESP protocol, errors are typically represented as simple strings,
 * but this interface allows for future extensibility if more complex error types are needed.
 * Also, by convention, the protocol does use the first word of the error message as the error type,
 * might as well enforce convention as a best practice.
 */

public interface ErrorType {}
