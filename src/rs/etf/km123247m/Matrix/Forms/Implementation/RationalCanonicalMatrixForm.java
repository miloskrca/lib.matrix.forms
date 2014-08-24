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
     * Matrix located left from smith form matrix.
     * Affected by operations on columns.
     */
    private IMatrix p;
    /**
     * Matrix located beneath from smith form matrix.
     * Affected by operations on rows.
     */
    private IMatrix q;

    /**
     * Utility matrix. x*I - A. Transformed to Smith normal form.
     * I - Diagonal matrix with all elements equal to 1
     * x - x symbol
     * A - smithMatrix
     */
    private IMatrix xIminusA;

    /**
     * Matrix transformed to Rational-Canonical form.
     */
    private IMatrix startMatrix;

    /**
     * Final result of the transformation.
     */
    private IMatrix finalMatrix;

    public RationalCanonicalMatrixForm(MatrixHandler handler) throws Exception {
        super(handler);
        startMatrix = handler.getMatrix();
        int rowNumber = startMatrix.getRowNumber();
        int columnNumber = startMatrix.getColumnNumber();
        finalMatrix = new ArrayMatrix(rowNumber, columnNumber);
        xIminusA = new ArrayMatrix(rowNumber, columnNumber);
        p = new ArrayMatrix(rowNumber, columnNumber);
        q = new ArrayMatrix(rowNumber, columnNumber);
        Object zero = ((PolynomialMatrixHandler) handler).getZero();
        Object one = ((PolynomialMatrixHandler) handler).getOne();
        for (int row = 0; row < rowNumber; row++) {
            for (int column = 0; column < columnNumber; column++) {
                if (row == column) {
                    xIminusA.set(new MatrixCell(row, column, getHandler().getSymbol('x')));
                    p.set(new MatrixCell(row, column, one));
                    q.set(new MatrixCell(row, column, one));
                } else {
                    xIminusA.set(new MatrixCell(row, column, zero));
                    p.set(new MatrixCell(row, column, zero));
                    q.set(new MatrixCell(row, column, zero));
                }
            }
        }
        handler.setMatrix(xIminusA);
        handler.minus(startMatrix);
    }

    @Override
    protected void process() throws Exception {
        SmithMatrixForm form = new SmithMatrixForm(getHandler());
        form.addObserver(this);
        form.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        FormEvent event = (FormEvent) arg;
        MatrixForm smithMatrixForm = (MatrixForm) o;
        // handle status events, ignore start and end events
        if (event.getType() == FormEvent.PROCESSING_STATUS) {
            ICommand lastCommand = smithMatrixForm.getCommands().getLast();
            try {
                if (lastCommand.affectsColumns()) {
                    getHandler().setMatrix(getP());
                    lastCommand.execute(getHandler());
                }
                if (lastCommand.affectsRows()) {
                    getHandler().setMatrix(getQ());
                    lastCommand.execute(getHandler());
                }
            } catch (Exception e) {
                sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage());
            } finally {
                // return original matrix to handler
                getHandler().setMatrix(xIminusA);
                // pass the event
                sendUpdate(event.getType(), event.getMessage());
            }
        } else if (event.getType() == FormEvent.PROCESSING_EXCEPTION) {
            // pass the event
            sendUpdate(event.getType(), event.getMessage());
        } else if (event.getType() == FormEvent.PROCESSING_END) {
            // Smith transformation ended
            // don't pass the event
            try {
                generateMatrixInRationalCanonicalForm();
            } catch (Exception e) {
                sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage());
            }
        }
    }

    protected abstract void generateMatrixInRationalCanonicalForm() throws Exception;

    /**
     * Matrix located left.
     *
     * @return IMatrix
     */
    public IMatrix getP() {
        return p;
    }

    /**
     * Matrix located beneath.
     *
     * @return IMatrix
     */
    public IMatrix getQ() {
        return q;
    }

    /**
     * Matrix transformed(ing) to rational-canonical form.
     *
     * @return IMatrix
     */
    public IMatrix getTransitionalMatrix() {
        return xIminusA;
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
}
