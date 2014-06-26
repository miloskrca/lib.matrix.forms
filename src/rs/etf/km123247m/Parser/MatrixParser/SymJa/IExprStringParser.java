package rs.etf.km123247m.Parser.MatrixParser.SymJa;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import rs.etf.km123247m.Parser.MatrixParser.Polynomial.PolynomialStringParser;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;
import rs.etf.km123247m.Polynomial.Polynomial;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class IExprStringParser extends StringParser {

    private EvalUtilities util = new EvalUtilities();
    private PolynomialStringParser polynomialStringParser;
    private boolean nativeInput;

    public IExprStringParser(boolean nativeInput) {
        super();
        F.initSymbols(null);
        this.nativeInput = nativeInput;
        if(!nativeInput) {
            this.polynomialStringParser = new PolynomialStringParser();
        }
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
        if(nativeInput) {
            // native: Plus[Times[-1, x], Power[x, 2]]
            return util.evaluate(input);
        } else {
            //non native: -x+x^2
            polynomialStringParser.setInputString(input);
            Polynomial poly = (Polynomial) polynomialStringParser.parseInput();
            return util.evaluate(poly.toString());
        }
    }

    @Override
    protected Object postObjectGeneration(Object o) throws Exception {
        // do nothing
        return o;
    }
}