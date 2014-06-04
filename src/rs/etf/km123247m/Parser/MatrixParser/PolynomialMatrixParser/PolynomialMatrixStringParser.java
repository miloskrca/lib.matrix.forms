package rs.etf.km123247m.Parser.MatrixParser.PolynomialMatrixParser;

import rs.etf.km123247m.Parser.MatrixParser.MatrixStringParser;
import rs.etf.km123247m.Parser.PolynomialParser.PolynomialStringParser;
import rs.etf.km123247m.Polynomial.Polynomial;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class PolynomialMatrixStringParser extends MatrixStringParser {

    private PolynomialStringParser polynomialStringParser = new PolynomialStringParser();

    @Override
    protected Polynomial createMatrixElement(String s) throws Exception {
        polynomialStringParser.setInputString(s);
        return (Polynomial) polynomialStringParser.parseInput();
    }

    @Override
    protected void postObjectGenerationChecks(Object o) throws Exception {
        // do nothing
    }
}
