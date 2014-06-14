package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Polynomial.Polynomial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of matrix operations using SymJa library
 *
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Handler.Implementation.SymJa
 */
public class SymJaMatrixHandler extends MatrixHandler {

    private EvalUtilities util = new EvalUtilities();

    public SymJaMatrixHandler(IMatrix matrix) {
        super(matrix);
        F.initSymbols(null);
    }

    @Override
    protected Object addElements(Object element1, Object element2) throws Exception {
        IExpr result = evaluate(((IExpr) element1).plus((IExpr) element2));
        return result;
    }

    @Override
    protected Object multiplyElements(Object element1, Object element2) throws Exception {
        IExpr result = evaluate(((IExpr) element1).multiply((IExpr) element2));
        return result;
    }

    @Override
    protected Object divideElements(Object element1, Object element2) throws Exception {
        IExpr result = evaluate(((IExpr) element1).divide((IExpr) element2));
        return result;
    }

    @Override
    public Object calculateNegativeElement(Object element) throws Exception {
        IExpr result = evaluate(((IExpr) element).negate());
        return result;
    }

    @Override
    public Object getZeroElement() throws Exception {
        IExpr result = evaluate(Polynomial.getZeroPolynomial());
        return result;
    }

    @Override
    public int compareElements(Object element1, Object element2) {
        int power1 = getHighestPower(element1);
        int power2 = getHighestPower(element2);
        int result = power1 > power2 ? 1 : (power1 < power2 ? -1 : 0);
        return result;
    }

    protected IExpr evaluate(Object expr) throws Exception {
        return util.evaluate("Expand[" + expr.toString() + "]");
    }

    protected int getHighestPower(Object expr) {
        int power = 1;
        String exprString = expr.toString().replace(" ", "");

        Pattern p = Pattern.compile("Power\\[(.),(\\d+)\\]");
        Matcher m = p.matcher(exprString);

        while(m.find()) {
            int groupPower = Integer.parseInt(m.group(2));
            if(groupPower > power) {
                power = groupPower;
            }
        }

        return power;
    }
}