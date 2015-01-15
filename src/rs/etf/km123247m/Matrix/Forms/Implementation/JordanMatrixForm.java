package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.Implementation.EJMLPolynomialHandler;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Observer.FormObserver;
import rs.etf.km123247m.Polynomial.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

/**
 * Created by Miloš Krsmanović.
 * Jun 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class JordanMatrixForm extends MatrixForm implements FormObserver {

    /**
     * Matrix transformed to Rational-Canonical form.
     */
    private IMatrix startMatrix;

    /**
     * Transitional matrix.
     */
    private IMatrix transitionalMatrix;

    /**
     * Final result of the transformation.
     */
    private IMatrix finalMatrix;

    /**
     * Found factors.
     */
    private ArrayList<Object> factors = new ArrayList<Object>();

    public JordanMatrixForm(MatrixHandler handler) throws Exception {
        super(handler);
        startMatrix = handler.duplicate(handler.getMatrix());
        int rowNumber = startMatrix.getRowNumber();
        int columnNumber = startMatrix.getColumnNumber();
        finalMatrix = startMatrix.createMatrix(rowNumber, columnNumber);
        Object zero = ((PolynomialMatrixHandler) handler).getZero();
        finalMatrix.initWith(zero);
        transitionalMatrix = handler.diagonal(startMatrix.getRowNumber(), getHandler().getObjectFromString("x"));

        transitionalMatrix = handler.subtract(transitionalMatrix, startMatrix);
        handler.setMatrix(transitionalMatrix);
    }

    @Override
    protected void process() throws Exception {
        getHandler().setMatrix(startMatrix);
        if (getHandler().matrixContainsSymbol()) {
            sendUpdate(FormEvent.PROCESSING_EXCEPTION, "Matrix is not numerical.", null);
            return;
        }
        getHandler().setMatrix(transitionalMatrix);

        sendUpdate(FormEvent.PROCESSING_START, null, getStartMatrix());
        MatrixForm form = new SmithMatrixForm(getHandler());
        form.addObserver(this);
        form.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        FormEvent event = (FormEvent) arg;
        MatrixForm smithMatrixForm = (MatrixForm) o;
        if (event.getType() == FormEvent.PROCESSING_STEP) {
            this.getCommands().add(smithMatrixForm.getCommands().getLast());
            // pass the event
            sendUpdate(event.getType(), event.getMessage(), event.getMatrix());
        } else if (event.getType() == FormEvent.PROCESSING_START) {
            // ignore
        } else if (event.getType() == FormEvent.PROCESSING_EXCEPTION
                || event.getType() == FormEvent.PROCESSING_INFO) {
            // pass the event
            sendUpdate(event.getType(), event.getMessage(), event.getMatrix());
        } else if (event.getType() == FormEvent.PROCESSING_END) {
            // Smith transformation ended
            try {
                postProcessTransitionalMatrix2();
                sendUpdate(FormEvent.PROCESSING_INFO, "Factored elements on diagonal.", transitionalMatrix);
//                generateJMatrix();
                sendUpdate(FormEvent.PROCESSING_END, null, getFinalMatrix());
            } catch (Exception e) {
                e.printStackTrace();
                sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage(), event.getMatrix());
            }
        }
    }


    // Work in progress
    protected void postProcessTransitionalMatrix2() throws Exception {
        PolynomialMatrixHandler handler = ((PolynomialMatrixHandler) getHandler());
        EJMLPolynomialHandler polyHandler = new EJMLPolynomialHandler();
        ArrayList<Object> roots = new ArrayList<Object>();
        for (int row = 0; row < transitionalMatrix.getRowNumber(); row++) {
            for (int column = 0; column < getTransitionalMatrix().getColumnNumber(); column++) {
                Object currentElement = transitionalMatrix.get(row, column).getElement();
                if (handler.compare(currentElement, handler.getZero()) != 0
                        && handler.compare(currentElement, handler.getOne()) != 0) {

                    // First try to do it using SymJa
                    // SymJa way
                    Object factoredCurrentElement = handler.factor(currentElement);
                    String factorsString;

                    if(!currentElement.toString().equals(factoredCurrentElement.toString()) && !factoredCurrentElement.toString().contains(Term.X + "^")) {
                        factorsString = factoredCurrentElement.toString();
                        factors.addAll(((PolynomialMatrixHandler) getHandler()).getFactorsFromElement(factoredCurrentElement));
                    } else {
                        // Factorization using SymJa failed
                        int highestPower = handler.getHighestPower(currentElement);
                        switch (highestPower) {
                            case -2:
                                // "By hand" way using quadratic formula
                                // NOT USED
                                Object[] qRoots = ((PolynomialMatrixHandler) getHandler()).quadraticFormula(currentElement);
                                Collections.addAll(roots, qRoots);
                                factorsString = handler.getFactorsStringFromRoots(qRoots);
                                break;
                            default:
                                // EJML way
                                double[] coefficients = new double[highestPower + 1];
                                for (int i = 0; i <= highestPower; i++) {
                                    coefficients[i] = Double.parseDouble(handler.getCoefficientForPower(currentElement, i).toString());
                                }

                                Object[] tempRoots = polyHandler.findRoots(coefficients);
                                Collections.addAll(roots, polyHandler.formatRoots(tempRoots));
                                factorsString = polyHandler.mergeRoots(tempRoots);
                        }
                    }

                    transitionalMatrix.set(new MatrixCell(row, column, factorsString));

                }
            }
        }

//        System.out.println();
//        System.out.println(transitionalMatrix);

        if(factors.size() > 0) {
            // SymJa way
            generateBlocks();
        } else {
            // EJML way
            int index = 0;
            int row = 0;
            while(row < finalMatrix.getRowNumber()) {
                int power = 1;
                // @TODO: detect repeating roots when not using CoefficientPowerPair
                if(power > 1) {
                    // add ones in diagonal above the main diagonal
                    // but only inside the current block
                    for(int row1 = row; row1 < row + power - 1; row1++) {
                        Object one = handler.getOne();
                        finalMatrix.set(new MatrixCell(row1, row1 + 1, one));
                    }
                }
                for(int row1 = row; row1 < row + power; row1++) {
                    finalMatrix.set(new MatrixCell(row1, row1, roots.get(index)));
                }

                row += power;
                index++;
            }
        }
    }







    protected void postProcessTransitionalMatrix() throws Exception {
        PolynomialMatrixHandler handler = ((PolynomialMatrixHandler)getHandler());
        for (int row = 0; row < transitionalMatrix.getRowNumber(); row++) {
            for (int column = 0; column < getTransitionalMatrix().getColumnNumber(); column++) {
                Object temp = transitionalMatrix.get(row, column).getElement();
                if(handler.compare(temp, handler.getZero()) != 0
                        && handler.compare(temp, handler.getOne()) != 0) {
                    temp = handler.factor(temp);
                    transitionalMatrix.set(new MatrixCell(row, column, temp));
                }
            }
        }
    }

    protected void generateJMatrix() throws Exception {
        ArrayList<Object> elements = new ArrayList<Object>();
        for (int row = 0; row < transitionalMatrix.getRowNumber(); row++) {
            for (int column = 0; column < transitionalMatrix.getColumnNumber(); column++) {
                Object element = transitionalMatrix.get(row, column).getElement();
                if(!getHandler().isZeroElement(element)
                        && getHandler().compare(getHandler().getObjectFromString("1"), element) != 0) {
                    elements.add(element);
                }
            }
        }
        try {
            for(Object element: elements) {
                factors.addAll(((PolynomialMatrixHandler)getHandler()).getFactorsFromElement(element));
            }
        } catch (Exception e) {
            throw new Exception("Factors couldn't be created");
        }
        generateBlocks();
    }

    private void generateBlocks() throws Exception {
        final PolynomialMatrixHandler handler = (PolynomialMatrixHandler)getHandler();
        //generate a list of power/coefficient pairs
        ArrayList<CoefficientPowerPair> pairs = new ArrayList<CoefficientPowerPair>();
        for(Object factor: factors) {
            CoefficientPowerPair pair = handler.getCoefficientPowerPairFromFactor(factor);
            Object coefficient = pair.getCoefficient();
            pair.setCoefficient(handler.calculateNegativeElement(coefficient));
            pairs.add(pair);
        }

        int index = 0;
        int row = 0;
        while(row < finalMatrix.getRowNumber()) {
            int power = Integer.parseInt(pairs.get(index).getPower().toString());
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
     * @param blockPair Coefficient-Power pars gotten from
     *                   corresponding polynomial from which the block is generated.
     * @throws Exception
     */
    protected void setBlock(int startRow, int size, CoefficientPowerPair blockPair) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        if(size > 1) {
            // add ones in diagonal above the main diagonal
            // but only inside the current block
            for(int row = startRow; row < startRow + size - 1; row++) {
                Object one = handler.getOne();
                finalMatrix.set(new MatrixCell(row, row + 1, one));
            }
        }
        for(int row = startRow; row < startRow + size; row++) {
            finalMatrix.set(new MatrixCell(row, row, blockPair.getCoefficient()));
        }
    }

    /**
     * Matrix to be transformed to rational-canonical form.
     *
     * @return IMatrix
     */
    public IMatrix getStartMatrix() {
        return startMatrix;
    }

    /**
     * Final transitional matrix.
     *
     * @return IMatrix
     */
    public IMatrix getTransitionalMatrix() {
        return transitionalMatrix;
    }

    /**
     * Final transformed matrix.
     *
     * @return IMatrix
     */
    public IMatrix getFinalMatrix() {
        return finalMatrix;
    }

}
