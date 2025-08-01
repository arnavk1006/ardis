package resp.parser.impl;

import resp.parser.ParserType;
import resp.parser.RespInputStream;
import resp.parser.RespParser;
import resp.types.RespArray;
import resp.types.RespType;
import java.io.IOException;

/**
 * Parser for RESP Arrays (identified by '*').
 * Arrays can contain nested RESP types, including other arrays.
 * They can also be used to represent nil values when the length is -1,
 * similar to Bulk Strings.
 */
public class ParseArray implements ParserType {

    /**
     * Parses a RESP Array from the input stream.
     *
     * @param respInputStream the RESP input stream
     * @return a {@link RespArray}, or null if the array is nil
     * @throws IOException potentially, because of the {@link RespInputStream} methods used
     * @throws IllegalArgumentException if the length of the array is invalid
     */
    @Override
    public RespType parse(RespInputStream respInputStream) throws IOException {
        int length = Integer.parseInt(respInputStream.readLine());

        if (length == -1) {
            return new RespArray(null); // Null array
        } else if (length < 0) {
            throw new IllegalArgumentException("Invalid array length: " + length +
                    ". Length must be either -1 or a non-negative number");
        }

        RespType[] elements = new RespType[length];
        for (int index = 0; index < length; index++) {
            elements[index] = RespParser.parse(respInputStream);
        }

        return new RespArray(elements);
    }
}
