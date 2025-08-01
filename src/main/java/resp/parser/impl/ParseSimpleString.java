package resp.parser.impl;

import resp.parser.ParserType;
import resp.parser.RespInputStream;
import resp.types.RespSimpleString;
import resp.types.RespType;
import java.io.IOException;

/**
 * Parser for RESP Simple Strings (identified by '+').
 * A Simple String is a line of text terminated by CRLF and does not contain binary-safe data.
 */
public class ParseSimpleString implements ParserType {

    /**
     * Parses a RESP Simple String from the input stream.
     *
     * @param respInputStream the RESP input stream
     * @return a {@link RespSimpleString} containing the parsed string
     * @throws IOException potentially, because of the {@link RespInputStream} methods used
     */
    @Override
    public RespType parse(RespInputStream respInputStream) throws IOException {
        String message = respInputStream.readLine();
        return new RespSimpleString(message);
    }
}
