package rs.etf.km123247m.Command;

import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command
 */
public interface ICommand {
    Object execute(MatrixHandler handler) throws Exception;

    String getDescription();
    boolean affectsRows();
    boolean affectsColumns();
    ICommand copy();
    IMatrix getMatrixBefore();
    IMatrix getMatrixAfter();
}
