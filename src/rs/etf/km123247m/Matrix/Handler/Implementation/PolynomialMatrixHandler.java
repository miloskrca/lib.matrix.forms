package rs.etf.km123247m.Matrix.Handler.Implementation;

import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;

import java.util.ArrayList;
import java.util.Collection;

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

    public Object getLeadingCoefficient(Object element) throws Exception {
        return getLeadingCoefficientOfElement(element);
    }

    public Object getInverse(Object element) throws Exception {
        return divideElements(getElementEquivalentToOne(), element);
    }

    public abstract ArrayList<CoefficientPowerPair> getCoefficientPowerPairs(Object element) throws Exception;

    protected abstract Object getElementEquivalentToOne() throws Exception;
    protected abstract Object getElementEquivalentToZero() throws Exception;
    protected abstract Object getLeadingCoefficientOfElement(Object element) throws Exception;

    public abstract boolean hasElementWithPower(Object element, int power) throws Exception;
    public abstract Object getCoefficientForPower(Object element, int power) throws Exception;
    public abstract Object factor (Object element) throws Exception;
    public abstract Collection<Object> getFactorsFromElement(Object element) throws Exception;
    public abstract CoefficientPowerPair getCoefficientPowerPairFromFactor(Object factor) throws Exception;
}
