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
public class MultiplyRowWithElementAndStoreCommand implements ICommand {

    private int row;
    private Object element;

    public MultiplyRowWithElementAndStoreCommand(int row, Object element) {
        this.row = row;
        this.element = element;
    }

    @Override
    public MatrixCell[] execute(MatrixHandler handler) throws Exception {
        MatrixCell[] rowCells = handler.multipleRowWithElement(row, element);
        handler.storeRow(row, rowCells);
        return rowCells;
    }

    public int getRow() {
        return row;
    }

    public Object getElement() {
        return element;
    }
}
