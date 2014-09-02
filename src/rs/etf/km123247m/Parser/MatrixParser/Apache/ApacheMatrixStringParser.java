package rs.etf.km123247m.Parser.MatrixParser.Apache;

import rs.etf.km123247m.Parser.MatrixParser.MatrixStringParser;
import rs.etf.km123247m.Polynomial.MathITPolynomial;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class ApacheMatrixStringParser extends MatrixStringParser {

    private ApacheStringParser apacheStringParser;

    public ApacheMatrixStringParser() {
        super();
        apacheStringParser = new ApacheStringParser();
    }

    @Override
    protected MathITPolynomial createMatrixElement(String s) throws Exception {
        apacheStringParser.setInputString(s);
        return (MathITPolynomial) apacheStringParser.parseInput();
    }

    @Override
    protected Object postObjectGeneration(Object o) throws Exception {
        // do nothing
        return o;
    }
}
