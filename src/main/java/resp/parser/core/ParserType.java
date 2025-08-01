package resp.parser.core;

import resp.types.RespType;
import java.io.IOException;

/**
 * Strategy interface for parsing RESP data types.
 * Implementations of this interface encapsulate the parsing logic for specific RESP types
 * This interface is used by the {@link resp.parser.core.RespParser} to delegate parsing
 * based on the RESP type indicator byte.
 */
public interface ParserType {
    /**
     * Parses a RESP type-specific value from the given input stream.
     *
     * @param respInputStream the RESP input stream to read from
     * @return the parsed {@link RespType} object
     * @throws IOException if an I/O error occurs. Stems from the {@link RespInputStream} methods used
     * @throws IllegalArgumentException for implementation-specific reasons. Refer to the individual parser
     * implementations
     */
    RespType parse(RespInputStream respInputStream) throws IOException, IllegalArgumentException;
}
