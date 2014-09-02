package rs.etf.km123247m.Parser.MatrixParser.Apache;

import rs.etf.km123247m.Parser.MatrixParser.MatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;

import java.io.File;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class ApacheMatrixFileParser extends MatrixFileParser {

    /**
     * File parser that parses a matrix with elements in Apache Common form
     * @param file File
     */
    public ApacheMatrixFileParser(File file) {
        super(file);
    }

    @Override
    protected StringParser instantiateStringParser() {
        return new ApacheMatrixStringParser();
    }

}
