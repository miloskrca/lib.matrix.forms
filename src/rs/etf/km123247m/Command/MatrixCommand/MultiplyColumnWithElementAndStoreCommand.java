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
public class MultiplyColumnWithElementAndStoreCommand implements ICommand {

    private int column;
    private Object element;

    public MultiplyColumnWithElementAndStoreCommand(int column, Object element) {
        this.column = column;
        this.element = element;
    }

    @Override
    public Object execute(MatrixHandler handler) throws Exception {
        MatrixCell[] columnCells = handler.multipleColumnWithElement(column, element);
        handler.storeColumn(column, columnCells);
        return columnCells;
    }

    public int getColumn() {
        return column;
    }

    public Object getElement() {
        return element;
    }
}
