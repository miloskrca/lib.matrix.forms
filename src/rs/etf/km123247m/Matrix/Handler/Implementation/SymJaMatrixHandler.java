package rs.etf.km123247m.Matrix.Handler.Implementation;

import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
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
    public SymJaMatrixHandler(IMatrix matrix) {
        super(matrix);
    }

    @Override
    protected Object addElements(Object element1, Object element2) {
        return null;
    }

    @Override
    protected Object multiplyElements(Object element1, Object element2) {
        return null;
    }

    @Override
    protected Object divideElements(Object element1, Object element2) {
        return null;
    }

    @Override
    public Object calculateNegativeElement(Object element) {
        return null;
    }

    @Override
    public Object getZeroElement() {
        return Polynomial.getZeroPolynomial();
    }

    @Override
    public int compareElements(Object element1, Object element2) {
        return 0;
    }
}