package resp.parser.impl;

import resp.parser.ParserType;
import resp.parser.RespInputStream;
import resp.types.RespInteger;
import resp.types.RespType;
import java.io.IOException;

/**
 * Parser for RESP Integers (identified by ':').
 * RESP Integers are 64-bit signed numbers represented as text and terminated by CRLF.
 * TODO: Implement support for the additional "+" or "-" sign which is optional.
 */
public class ParseInteger implements ParserType {

    /**
     * Parses a RESP Integer from the input stream.
     *
     * @param respInputStream the RESP input stream
     * @return a {@link RespInteger} containing the parsed long value
     * @throws IOException potentially, because of the {@link RespInputStream} methods used
     */
    @Override
    public RespType parse(RespInputStream respInputStream) throws IOException {
        long value = Long.parseLong(respInputStream.readLine());
        return new RespInteger(value);
    }
}
