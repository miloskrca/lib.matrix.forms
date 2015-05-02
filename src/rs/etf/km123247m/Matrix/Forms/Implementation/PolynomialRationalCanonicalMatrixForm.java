package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observer.Event.FormEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    /**
     * Generates a matrix in rational canonical form based on matrix xIminusA
     * that was previously transformed to Smith form.
     *
     * @throws Exception
     */
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

    /**
     * Generates and adds a block to final matrix.
     *
     * @param startRow Row the block starts from.
     * @param size Size of the block.
     * @param blockPairs Coefficient-Power pars gotten from
     *                   corresponding polynomial from which the block is generated.
     * @throws Exception
     */
    protected void setBlock(int startRow, int size, ArrayList<CoefficientPowerPair> blockPairs) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        if(size > 1) {
            // add ones in diagonal below the main diagonal
            // but only inside the current block
            for(int row = startRow + 1; row < startRow + size; row++) {
                Object one = handler.getOne();
                getFinalMatrix().set(new MatrixCell(row, row - 1, one));
            }
        }
        int power = 0;
        for(int row = startRow; row < startRow + size; row++) {
            // (negative) coefficient to be added to block
            Object coefficient = getCoefficientForElementWithPower(blockPairs, power++);
            Object coefficientObject = getHandler().getObjectFromString(String.valueOf(coefficient));
            if(!handler.isZeroElement(coefficientObject)) {
                coefficientObject = handler.calculateNegativeElement(coefficientObject);
            }
            getFinalMatrix().set(new MatrixCell(row, startRow + size - 1, coefficientObject));
        }
    }

    /**
     * Returns coefficient of the element with requested power.
     *
     * @param pairs Coefficient-Power pairs which will be iterated.
     * @param power Power of the element from which the coefficient is requested.
     * @return Returns coefficient of the element with requested power.
     * @throws Exception
     */
    protected Object getCoefficientForElementWithPower(ArrayList<CoefficientPowerPair> pairs, int power) throws Exception {
        Iterator<CoefficientPowerPair> iterator = pairs.iterator();
        Object coefficient = null;
        while (iterator.hasNext()) {
            CoefficientPowerPair pair = iterator.next();
            Object powerObject = getHandler().getObjectFromString(String.valueOf(pair.getPower()));
            if(getHandler().compare(powerObject, getHandler().getObjectFromString(String.valueOf(power))) == 0) {
                coefficient = pair.getCoefficient();
            }
        }
        // if the requested element is not located in the pars array
        // then it means that the coefficient is equal to 0
        if(coefficient == null) {
            coefficient = getHandler().getObjectFromString("0");
        }

        return coefficient;
    }

    /**
     * Returns the highest power from the supplied Coefficient-Power pairs.
     *
     * @param pairs Coefficient-Power pairs to be iterated.
     * @return Returns the highest power from the supplied Coefficient-Power pairs.
     * @throws Exception
     */
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


    @Override
    protected void generateMatrixT() throws Exception {
        IMatrix invertedP0 = getHandler().invertMatrix(getP(0));
        IMatrix p = new ArrayMatrix(
                getHandler().getMatrix().getRowNumber(),
                getHandler().getMatrix().getColumnNumber()
        );
        p.initWith(getHandler().getObjectFromString("0"));
        getHandler().multiply(invertedP0, getP(1), p);

        sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_RATIONAL_GENERATE_PX, p);
        calcT(getStartMatrix(), p);
    }

    protected void calcT(IMatrix startMatrix, IMatrix matrix) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        int rows = startMatrix.getRowNumber();
        int columns = startMatrix.getColumnNumber();
        IMatrix t = null;

        setpMatrices(new HashMap<Integer, IMatrix>());
        int highestPower = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Object item = matrix.get(i, j).getElement();
                int deg = getHandler().getHighestPower(item);
                if(deg > highestPower) {
                    highestPower = deg;
                }
                while(deg >= 0) {
                    if(handler.hasElementWithPower(item, deg)) {
                        Object coefficient = handler.getCoefficientForPower(item, deg).toString();
                        initMatrix(getpMatrices(), deg, rows);
                        getpMatrices().get(deg).set(new MatrixCell(i, j, handler.getObjectFromString(coefficient.toString())));
                    }
                    deg--;
                }
            }
        }

        for (int power = 0; power <= highestPower; power++) {
            if(getpMatrices().containsKey(power)) {
                if(t == null) {
                    t = new ArrayMatrix(rows, columns);
                    t.initWith(handler.getObjectFromString("0"));
                }
                // tempResult = A^n*Pn
                IMatrix tempResult = handler.multiply(handler.power(startMatrix, power), getpMatrices().get(power));
                // t = t + A^n*Pn
                t = handler.add(t, tempResult);
            }
        }

        setT(t);
        sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_RATIONAL_END_GENERATE_PX, matrix);
    }

    protected void initMatrix(HashMap<Integer, IMatrix> pMatrices, int power, int range) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        if(!pMatrices.containsKey(power)) {
            IMatrix matrix = new ArrayMatrix(range, range);
            matrix.initWith(handler.getZero());
            pMatrices.put(power, matrix);
        }
    }

}
