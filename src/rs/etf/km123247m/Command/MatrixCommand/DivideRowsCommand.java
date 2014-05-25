package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class DivideRowsCommand implements ICommand {
    private final MatrixHandler handler;
    private final int row1;
    private final int row2;
    private final int column;

    public DivideRowsCommand(MatrixHandler handler, int row1, int row2, int column) {
        this.handler = handler;
        this.row1 = row1;
        this.row2 = row2;
        this.column = column;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public int getRow1() {
        return row1;
    }

    public int getRow2() {
        return row2;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public Object execute() {
        return null;
    }
}
