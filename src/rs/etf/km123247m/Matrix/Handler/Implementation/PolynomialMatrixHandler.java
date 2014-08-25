package rs.etf.km123247m.Matrix.Handler.Implementation;

import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;

import java.util.ArrayList;

/**
 * Created by Miloš Krsmanović.
 * Jun 2014
 *
 * This class is used for adding additional Polynomial related functionality to MatrixHandler.
 * Decorator pattern wasn't used because it added too much complexity since MatrixHandler has a lot of public functions.
 *
 * package: rs.etf.km123247m.Matrix.Handler.Implementation
 */
public abstract class PolynomialMatrixHandler extends MatrixHandler {

    protected PolynomialMatrixHandler(IMatrix matrix) {
        super(matrix);
    }

    public Object getOne() throws Exception {
        return getElementEquivalentToOne();
    }
    public Object getZero() throws Exception {
        return getElementEquivalentToZero();
    }

    public int getPower(MatrixCell cell) {
        return getHighestPower(cell.getElement());
    }

    public Object getLeadingCoefficient(MatrixCell cell) throws Exception {
        return getLeadingCoefficientOfElement(cell.getElement());
    }

    public Object getInverse(Object element) throws Exception {
        return divideElements(getElementEquivalentToOne(), element);
    }

    public abstract ArrayList<CoefficientPowerPair> getCoefficientPowerPairs(Object element) throws Exception;

    protected abstract Object getElementEquivalentToOne() throws Exception;
    protected abstract Object getElementEquivalentToZero() throws Exception;
    protected abstract int getHighestPower(Object element);

    protected abstract Object getLeadingCoefficientOfElement(Object element) throws Exception;
}
