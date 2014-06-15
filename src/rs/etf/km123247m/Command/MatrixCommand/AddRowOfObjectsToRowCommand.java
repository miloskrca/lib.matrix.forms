package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class AddRowOfObjectsToRowCommand implements ICommand {

    private MatrixHandler handler;
    private int row1;
    private MatrixCell[] objectRow;

    public AddRowOfObjectsToRowCommand(MatrixHandler handler, int row1, MatrixCell[] objectRow) {
        this.handler = handler;
        this.row1 = row1;
        this.objectRow = objectRow;
    }

    @Override
    public Object execute() throws Exception {
        handler.addRows(row1, objectRow);
        return null;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public int getRow1() {
        return row1;
    }

    public MatrixCell[] getObjectRow() {
        return objectRow;
    }
}