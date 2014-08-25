package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Miloš Krsmanović.
 * Aug 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
// TODO: do security checks for power, matrix degree, number of polynomials, powers of the polynomials...
public class PolynomialRationalCanonicalMatrixForm extends RationalCanonicalMatrixForm {

    public PolynomialRationalCanonicalMatrixForm(MatrixHandler handler) throws Exception {
        super(handler);
    }

    @Override
    protected void generateMatrixInRationalCanonicalForm() throws Exception {
        // xIminusA is now in Smith normal form
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        ArrayList<Object> polynomialsOnTheDiagonal = new ArrayList<Object>();
        // matrix = xIminusA
        IMatrix matrix = handler.getMatrix();
        for(int row = 0; row < matrix.getRowNumber(); row++) {
            Object element = matrix.get(row, row).getElement();
            if(handler.compare(element, handler.getOne()) != 0) {
                polynomialsOnTheDiagonal.add(element);
            }
        }
        ArrayList<ArrayList<CoefficientPowerPair>> pairs = new ArrayList<ArrayList<CoefficientPowerPair>>();
        for (Object polynomialOnTheDiagonal : polynomialsOnTheDiagonal) {
            pairs.add(handler.getCoefficientPowerPairs(polynomialOnTheDiagonal));
        }

        int index = 0;
        int row = 0;
        while(row < matrix.getRowNumber()) {
            int power = getHighestPower(pairs.get(index));
            setBlock(row, power, pairs.get(index));
            row += power;
            index++;
        }

    }

    protected void setBlock(int startRow, int size, ArrayList<CoefficientPowerPair> blockPairs) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        IMatrix matrix = handler.getMatrix();
        if(size > 1) {
            for(int row = startRow + 1; row < startRow + size; row++) {
                Object one = handler.getOne();
                getFinalMatrix().set(new MatrixCell(row, row - 1, one));
            }
        }
        for(int row = startRow; row < startRow + size; row++) {
            Object element = getCoefficientForElementWithPower(blockPairs, row);
            if(!handler.isZeroElement(element)) {
                element = handler.calculateNegativeElement(element);
            }
            getFinalMatrix().set(new MatrixCell(row, matrix.getRowNumber() - 1, element));
        }
    }

    protected Object getCoefficientForElementWithPower(ArrayList<CoefficientPowerPair> pairs, int power) throws Exception {
        Iterator<CoefficientPowerPair> iterator = pairs.iterator();
        Object coefficient = null;
        while (iterator.hasNext()) {
            CoefficientPowerPair pair = iterator.next();
            if(getHandler().compare(pair.getPower(), getHandler().getObjectFromString(String.valueOf(power))) == 0) {
                coefficient = pair.getCoefficient();
            }
        }
        if(coefficient == null) {
            coefficient = getHandler().getObjectFromString("0");
        }

        return coefficient;
    }

    protected int getHighestPower(ArrayList<CoefficientPowerPair> pairs) throws Exception {
        Iterator<CoefficientPowerPair> iterator = pairs.iterator();
        int power = 0;
        while (iterator.hasNext()) {
            CoefficientPowerPair pair = iterator.next();
            int pairPower = Integer.parseInt(pair.getPower().toString());
            if(power < pairPower) {
                power = pairPower;
            }
        }

        return power;
    }

}
