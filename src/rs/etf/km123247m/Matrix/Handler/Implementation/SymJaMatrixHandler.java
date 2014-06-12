package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Parser.ObjectParser.PolynomialStringParser;
import rs.etf.km123247m.Polynomial.Polynomial;

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
        IExpr result = ((IExpr)element1).and((IExpr) element2);
        return result;
    }

    @Override
    protected Object multiplyElements(Object element1, Object element2) throws Exception {
        IExpr result = ((IExpr)element1).multiply((IExpr)element2);
        return result;
    }

    @Override
    protected Object divideElements(Object element1, Object element2) throws Exception {
        IExpr result = ((IExpr)element1).divide((IExpr) element2);
        return result;
    }

    @Override
    public Object calculateNegativeElement(Object element) throws Exception {
        IExpr result = ((IExpr)element).negate();
        return result;
    }

    @Override
    public Object getZeroElement() throws Exception {
        IExpr result = util.evaluate(Polynomial.getZeroPolynomial().toString());
        return result;
    }

    @Override
    public int compareElements(Object element1, Object element2) {
        int result = ((IExpr)element1).compareTo((IExpr) element2);
        return result;
    }
}