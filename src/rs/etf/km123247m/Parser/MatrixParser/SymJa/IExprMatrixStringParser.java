package rs.etf.km123247m.Parser.MatrixParser.SymJa;

import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Parser.MatrixParser.MatrixStringParser;
import rs.etf.km123247m.Parser.ObjectParser.IExprStringParser;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class IExprMatrixStringParser extends MatrixStringParser {

    private IExprStringParser iExprStringParser = new IExprStringParser();

    @Override
    protected IExpr createMatrixElement(String s) throws Exception {
        iExprStringParser.setInputString(s);
        return (IExpr) iExprStringParser.parseInput();
    }

    @Override
    protected void postObjectGenerationChecks(Object o) throws Exception {
        // do nothing
    }
}
