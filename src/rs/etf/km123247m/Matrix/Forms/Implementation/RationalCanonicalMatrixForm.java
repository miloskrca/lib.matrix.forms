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
public class RationalCanonicalMatrixForm extends MatrixForm implements FormObserver {

    /**
     * Matrix transformed to Smith matrix form
     */
    private IMatrix smithMatrix;
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

    public RationalCanonicalMatrixForm(MatrixHandler handler) throws Exception {
        super(handler);
        smithMatrix = handler.getMatrix();
        int rowNumber = smithMatrix.getRowNumber();
        int columnNumber = smithMatrix.getColumnNumber();
        p = new ArrayMatrix(rowNumber, columnNumber);
        q = new ArrayMatrix(rowNumber, columnNumber);
        Object zero = ((PolynomialMatrixHandler) handler).getZero();
        Object one = ((PolynomialMatrixHandler) handler).getOne();
        for (int row = 0; row < rowNumber; row++) {
            for (int column = 0; column < columnNumber; column++) {
                if (row == column) {
                    p.set(new MatrixCell(row, column, one));
                    q.set(new MatrixCell(row, column, one));
                } else {
                    p.set(new MatrixCell(row, column, zero));
                    q.set(new MatrixCell(row, column, zero));
                }
            }
        }
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
                getHandler().setMatrix(smithMatrix);
                // pass the event
                sendUpdate(event.getType(), event.getMessage());
            }
        } else if (event.getType() == FormEvent.PROCESSING_EXCEPTION) {
            // pass the event
            sendUpdate(event.getType(), event.getMessage());
        }
    }

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
     * Smith matrix.
     *
     * @return IMatrix
     */
    public IMatrix getSmithMatrix() {
        return smithMatrix;
    }
}
