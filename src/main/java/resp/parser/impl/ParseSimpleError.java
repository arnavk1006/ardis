package resp.parser.impl;

import resp.parser.ParserType;
import resp.parser.RespInputStream;
import resp.types.RespSimpleError;
import resp.types.RespType;
import java.io.IOException;

/**
 * Parser for RESP Simple Errors (identified by '-').
 * Simple Errors behave like Simple Strings but semantically represent error conditions.
 */
public class ParseSimpleError implements ParserType {

    /**
     * Parses a RESP Simple Error from the input stream.
     *
     * @param respInputStream the RESP input stream
     * @return a {@link RespSimpleError} containing the error message
     * @throws IOException potentially, because of the {@link RespInputStream} methods used
     */
    @Override
    public RespType parse(RespInputStream respInputStream) throws IOException {
        String message = respInputStream.readLine();
        return new RespSimpleError(message);
    }
}
