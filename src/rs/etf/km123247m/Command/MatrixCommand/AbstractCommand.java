package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command
 */
public abstract class AbstractCommand implements ICommand {

    private IMatrix matrixBefore;
    private IMatrix matrixAfter;

    public IMatrix getMatrixBefore() {
        return matrixBefore;
    }

    public IMatrix getMatrixAfter() {
        return matrixAfter;
    }

    public Object execute(MatrixHandler handler) throws Exception {
        matrixBefore = handler.duplicate(handler.getMatrix());
        Object toReturn = executeCommand(handler);
        matrixAfter = handler.duplicate(handler.getMatrix());

        return toReturn;
    }

    protected abstract Object executeCommand(MatrixHandler handler) throws Exception;
}
