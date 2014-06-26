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
public class AddColumnOfCellsToColumnCommand implements ICommand {

    private MatrixHandler handler;
    private int column1;
    private MatrixCell[] objectColumn;

    // TODO: This command shouldn't exist
    public AddColumnOfCellsToColumnCommand(MatrixHandler handler, int column1, MatrixCell[] objectColumn) {
        this.handler = handler;
        this.column1 = column1;
        this.objectColumn = objectColumn;
    }

    @Override
    public Object execute() throws Exception {
        handler.addColumns(column1, objectColumn);
        return null;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public int getColumn1() {
        return column1;
    }

    public MatrixCell[] getObjectColumn() {
        return objectColumn;
    }
}
