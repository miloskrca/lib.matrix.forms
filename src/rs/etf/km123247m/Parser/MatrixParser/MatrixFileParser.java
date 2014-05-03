package rs.etf.km123247m.Parser.MatrixParser;

import rs.etf.km123247m.Parser.ParserTypes.FileParser;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;

import java.io.File;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public abstract class MatrixFileParser extends FileParser {

    public MatrixFileParser(File file) {
        super(file);
    }

    @Override
    protected abstract StringParser instantiateStringParser();
}
