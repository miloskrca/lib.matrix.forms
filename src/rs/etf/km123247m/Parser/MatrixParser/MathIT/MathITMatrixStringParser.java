package rs.etf.km123247m.Parser.MatrixParser.MathIT;

import rs.etf.km123247m.Parser.MatrixParser.MatrixStringParser;
import rs.etf.km123247m.Polynomial.MathITPolynomial;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class MathITMatrixStringParser extends MatrixStringParser {

    private MathITStringParser mathITStringParser;

    public MathITMatrixStringParser() {
        super();
        mathITStringParser = new MathITStringParser();
    }

    @Override
    protected MathITPolynomial createMatrixElement(String s) throws Exception {
        mathITStringParser.setInputString(s);
        return (MathITPolynomial) mathITStringParser.parseInput();
    }

    @Override
    protected Object postObjectGeneration(Object o) throws Exception {
        // do nothing
        return o;
    }
}
