package rs.etf.km123247m.Parser.MatrixParser.SymJa;

import rs.etf.km123247m.Parser.MatrixParser.MatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;

import java.io.File;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class IExprMatrixFileParser extends MatrixFileParser {
    public IExprMatrixFileParser(File file) {
        super(file);
    }

    @Override
    protected StringParser instantiateStringParser() {
        return new IExprMatrixStringParser();
    }
}
