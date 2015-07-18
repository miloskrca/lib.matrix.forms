package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Observer.FormObserver;
import rs.etf.km123247m.Polynomial.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by Miloš Krsmanović.
 * Jun 2014
 * <p/>
 * The way the matrices are organised for easier visualization:
 * <p/>
 * |1 0||a b|
 * |0 1||c d|
 *      |1 0|
 *      |0 1|
 * <p/>
 * |P P||A A|
 * |P P||A A|
 *      |Q Q|
 *      |Q Q|
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public abstract class RationalCanonicalMatrixForm extends MatrixForm implements FormObserver {

    /**
     * Matrices located left from smith form matrix.
     * Affected by operations on rows.
     */
    private IMatrix[] p = new IMatrix[2];
    /**
     * Matrices located beneath from smith form matrix.
     * Affected by operations on columns.
     */
    private IMatrix[] q = new IMatrix[2];

    /**
     * Utility matrix. x*I - A. Transformed to Smith normal form.
     * I - Diagonal matrix with all elements equal to 1
     * x - x symbol
     * A - smithMatrix
     */
    private IMatrix xIminusA;

    /**
     * Utility matrix. x*I - B. Transformed to Smith normal form.
     * I - Diagonal matrix with all elements equal to 1
     * x - x symbol
     * B - matrix in rational form = finalMatrix
     */
    private IMatrix xIminusB;

    /**
     * Matrix transformed to Rational-Canonical form.
     */
    private IMatrix startMatrix;

    /**
     * Final result of the transformation.
     */
    private IMatrix finalMatrix;

    /**
     * Matrix T.
     */
    private IMatrix t;

    /**
     * Algorithm runs twice. This cunts the rounds.
     */
    private int round = 0;

    /**
     * Rational blocks
     */
    private ArrayList<IMatrix> rationalBlocks = new ArrayList<IMatrix>();

    /**
     * P matrices
     */
    private HashMap<Integer, IMatrix> pMatrices;

    public RationalCanonicalMatrixForm(MatrixHandler handler) throws Exception {
        super(handler);
        startMatrix = handler.getMatrix();
        int rowNumber = startMatrix.getRowNumber();
        int columnNumber = startMatrix.getColumnNumber();
        finalMatrix = new ArrayMatrix(rowNumber, columnNumber);
        xIminusA = new ArrayMatrix(rowNumber, columnNumber);
        xIminusB = new ArrayMatrix(rowNumber, columnNumber);
        p[0] = new ArrayMatrix(rowNumber, columnNumber);
        p[1] = new ArrayMatrix(rowNumber, columnNumber);
        q[0] = new ArrayMatrix(rowNumber, columnNumber);
        q[1] = new ArrayMatrix(rowNumber, columnNumber);
        Object zero = ((PolynomialMatrixHandler) handler).getZero();
        Object one = ((PolynomialMatrixHandler) handler).getOne();
        for (int row = 0; row < rowNumber; row++) {
            for (int column = 0; column < columnNumber; column++) {
                if (row == column) {
                    xIminusA.set(new MatrixCell(row, column, getHandler().getObjectFromString(String.valueOf(Term.X))));
                    xIminusB.set(new MatrixCell(row, column, getHandler().getObjectFromString(String.valueOf(Term.X))));
                    p[0].set(new MatrixCell(row, column, one));
                    p[1].set(new MatrixCell(row, column, one));
                    q[0].set(new MatrixCell(row, column, one));
                    q[1].set(new MatrixCell(row, column, one));
                } else {
                    xIminusA.set(new MatrixCell(row, column, zero));
                    xIminusB.set(new MatrixCell(row, column, zero));
                    p[0].set(new MatrixCell(row, column, zero));
                    p[1].set(new MatrixCell(row, column, zero));
                    q[0].set(new MatrixCell(row, column, zero));
                    q[1].set(new MatrixCell(row, column, zero));
                }
                finalMatrix.set(new MatrixCell(row, column, zero));
            }
        }
    }

    @Override
    protected void process() throws Exception {
        getHandler().setMatrix(startMatrix);
        if (getHandler().matrixNotNumerical()) {
            sendUpdate(FormEvent.PROCESSING_EXCEPTION, FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL, null);
            return;
        }
        sendUpdate(FormEvent.PROCESSING_START, null, getStartMatrix());
        getHandler().setMatrix(xIminusA);
        getHandler().subtractWith(startMatrix);
        sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_SUBTRACT_FOR_SMITH, getTransitionalMatrix(round));
        MatrixForm form = new SmithMatrixForm(getHandler());
        form.addObserver(this);
        form.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        FormEvent event = (FormEvent) arg;
        MatrixForm smithMatrixForm = (MatrixForm) o;
        if (event.getType() == FormEvent.PROCESSING_STEP) {
            ICommand lastCommand = smithMatrixForm.getCommands().getLast();
            try {
                ICommand command = lastCommand.copy();
                // using old commands is not a good idea, it overrides the
                // information of the processed matrix
                if (command.affectsColumns()) {
                    getHandler().setMatrix(getQ(round));
                    command.execute(getHandler());
                }
                if (command.affectsRows()) {
                    getHandler().setMatrix(getP(round));
                    command.execute(getHandler());
                }
            } catch (Exception e) {
                sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage(), getHandler().getMatrix());
            } finally {
                this.getCommands().add(lastCommand);
                // return original matrix to handler
                if(round == 0) {
                    getHandler().setMatrix(xIminusA);
                } else {
                    getHandler().setMatrix(xIminusB);
                }
                // pass the event
                sendUpdate(event.getType(), event.getMessage(), event.getMatrix());
            }
        } else if (event.getType() == FormEvent.PROCESSING_START) {
            // ignore
        } else if (event.getType() == FormEvent.PROCESSING_EXCEPTION
                || event.getType() == FormEvent.PROCESSING_INFO) {
            // pass the event
            sendUpdate(event.getType(), event.getMessage(), event.getMatrix());
        } else if (event.getType() == FormEvent.PROCESSING_END) {
            // Smith transformation ended

            if(round == 0) {
                try {
                    generateMatrixInRationalCanonicalForm();
                    sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_RATIONAL_FINISH, finalMatrix);
                } catch (Exception e) {
                    sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage(), event.getMatrix());
                }
                round = 1;

                try {
                    getHandler().setMatrix(xIminusB);
                    // finalMatrix = B
                    getHandler().subtractWith(finalMatrix);
                    sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_RATIONAL_PREPARE_T, getFinalMatrix());
                    MatrixForm form = new SmithMatrixForm(getHandler());
                    form.addObserver(this);
                    form.start();
                } catch (Exception e) {
                    sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage(), event.getMatrix());
                }
            } else {
                try {
                    sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_RATIONAL_GENERATE_T, finalMatrix);
                    generateMatrixT();
                    sendUpdate(FormEvent.PROCESSING_END, null, getFinalMatrix());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage(), event.getMatrix());
                }
            }
        }
    }

    protected abstract void generateMatrixT() throws Exception;

    protected abstract void generateMatrixInRationalCanonicalForm() throws Exception;

    /**
     * Matrix located left.
     *
     * @return IMatrix
     */
    public IMatrix getP(int round) {
        return p[round];
    }

    /**
     * Matrix located beneath.
     *
     * @return IMatrix
     */
    public IMatrix getQ(int round) {
        return q[round];
    }

    /**
     * Matrix transformed(ing) to rational-canonical form.
     *
     * @return IMatrix
     */
    public IMatrix getTransitionalMatrix(int round) {
        return round == 0 ? xIminusA : xIminusB;
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
     * Final transformed matrix.
     *
     * @return IMatrix
     */
    public IMatrix getFinalMatrix() {
        return finalMatrix;
    }

    public IMatrix getT() {
        return t;
    }

    protected void setT(IMatrix t) {
        this.t = t;
    }

    public int getRound() {
        return round;
    }

    public HashMap<Integer, IMatrix> getpMatrices() {
        return pMatrices;
    }

    public void setpMatrices(HashMap<Integer, IMatrix> pMatrices) {
        this.pMatrices = pMatrices;
    }

    public ArrayList<IMatrix> getRationalBlocks() {
        return rationalBlocks;
    }

    public void setRationalBlocks(ArrayList<IMatrix> rationalBlocks) {
        this.rationalBlocks = rationalBlocks;
    }
}
