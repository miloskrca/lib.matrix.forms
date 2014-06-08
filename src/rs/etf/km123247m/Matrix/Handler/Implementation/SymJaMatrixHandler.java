package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Parser.PolynomialParser.PolynomialStringParser;
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

    private PolynomialStringParser polynomialStringParser = new PolynomialStringParser();
    private EvalUtilities util = new EvalUtilities();


    public SymJaMatrixHandler(IMatrix matrix) {
        super(matrix);
        F.initSymbols(null);
    }

    @Override
    protected Object addElements(Object element1, Object element2) throws Exception {
        String input = element1.toString() + " + " + element2.toString();
        return generateObjectFromString(input);
    }

    @Override
    protected Object multiplyElements(Object element1, Object element2) throws Exception {
        String input = "(" + element1.toString() + ") * (" + element2.toString() + ")";
        return generateObjectFromString(input);
    }

    @Override
    protected Object divideElements(Object element1, Object element2) throws Exception {
        String input = "PolynomialQuotient[" + element1.toString() + "," + element2.toString() + "]";
        return generateObjectFromString(input);
    }

    @Override
    public Object calculateNegativeElement(Object element) throws Exception {
        String input = "-1 * (" + element.toString() + ")";
        return generateObjectFromString(input);
    }

    @Override
    public Object getZeroElement() {
        return Polynomial.getZeroPolynomial();
    }

    @Override
    public int compareElements(Object element1, Object element2) {
        return ((Polynomial)element1).compareTo(element2);
    }

    protected Object generateObjectFromString(String input) throws Exception {
        IExpr result = util.evaluate(input);
        StringBufferWriter buf = new StringBufferWriter();
        OutputFormFactory.get().convert(buf, result);
        String output = buf.toString();

        polynomialStringParser.setInputString(output);
        return polynomialStringParser.parseInput();
    }

}