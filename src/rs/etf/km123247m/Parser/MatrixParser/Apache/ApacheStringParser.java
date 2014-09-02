package rs.etf.km123247m.Parser.MatrixParser.Apache;

import rs.etf.km123247m.Parser.MatrixParser.Polynomial.PolynomialStringParser;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;
import rs.etf.km123247m.Polynomial.MathITPolynomial;
import rs.etf.km123247m.Polynomial.Polynomial;
import rs.etf.km123247m.Polynomial.Term;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class ApacheStringParser extends StringParser {

    private PolynomialStringParser polynomialStringParser;

    public ApacheStringParser() {
        super();
        this.polynomialStringParser = new PolynomialStringParser();
    }

    @Override
    protected void preInputParseChecks(String inputString) throws Exception {
        if(inputString == null) {
            throw new Exception("No string to parse");
        }
    }

    @Override
    protected String parseFromInput(String inputString) {
        // do nothing
        return inputString;
    }

    @Override
    protected void postInputParseChecks(String parsedInput) throws Exception {
        // do nothing
    }

    @Override
    protected Object generateObject(String input) throws Exception {
        polynomialStringParser.setInputString(input);
        Polynomial polynomial = (Polynomial) polynomialStringParser.parseInput();
        MathITPolynomial object = new MathITPolynomial();
        int termNumber = polynomial.getTerms().size();
        for (int i = 0; i < termNumber; i++) {
            Term term = polynomial.getTerm(i);
            char sign = term.getSign() == Term.MINUS ? Term.MINUS_CHAR : Term.PLUS_CHAR;
            object.put(term.getPower(), Double.parseDouble(sign + "" + term.getCoefficient()));
        }
        return object;
    }

    @Override
    protected Object postObjectGeneration(Object o) throws Exception {
        // do nothing
        return o;
    }
}