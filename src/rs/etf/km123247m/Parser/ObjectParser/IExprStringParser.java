package rs.etf.km123247m.Parser.ObjectParser;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;
import rs.etf.km123247m.Parser.ObjectParser.PolynomialStringParser;
import rs.etf.km123247m.Polynomial.Polynomial;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class IExprStringParser extends StringParser {

    private EvalUtilities util = new EvalUtilities();
    private PolynomialStringParser polynomialStringParser = new PolynomialStringParser();

    public IExprStringParser() {
        super();
        F.initSymbols(null);
    }

    @Override
    protected void preInputParseChecks(String inputString) throws Exception {
        if(inputString == null) {
            throw new Exception("No IExpr string to parse");
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
        Polynomial poly = (Polynomial) polynomialStringParser.parseInput();
        return util.evaluate(poly.toString());
    }

    @Override
    protected void postObjectGenerationChecks(Object o) throws Exception {
        // do nothing
    }
}