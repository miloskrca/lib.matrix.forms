package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Observer.FormObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        finalMatrix = new ArrayMatrix(rowNumber, columnNumber);
        Object zero = ((PolynomialMatrixHandler) handler).getZero();
        transitionalMatrix = handler.diagonal(startMatrix.getRowNumber(), getHandler().getObjectFromString("x"));
        finalMatrix.initWith(zero);

        handler.setMatrix(transitionalMatrix);
        handler.subtractWith(startMatrix);
    }

    @Override
    protected void process() throws Exception {
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
                postProcessTransitionalMatrix();
                sendUpdate(FormEvent.PROCESSING_INFO, "Factored elements on diagonal.", transitionalMatrix);
                findFactors();
                sendUpdate(FormEvent.PROCESSING_END, null, getFinalMatrix());
            } catch (Exception e) {
                e.printStackTrace();
                sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage(), event.getMatrix());
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

    protected void findFactors() throws Exception {
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
            if(handler.compare(handler.getZero(), coefficient) > 0) {
                pair.setCoefficient(handler.calculateNegativeElement(coefficient));
            }
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
