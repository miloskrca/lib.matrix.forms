package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;

import java.util.ArrayList;

/**
 * Created by Miloš Krsmanović.
 * Aug 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
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

//        int numberOfBlocks = polynomialsOnTheDiagonal.size();
        // @TODO: generate block and fill in the final matrix
    }
}
