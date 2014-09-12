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
public class MultiplyRowWithElementAndAddToRowAndStoreCommand implements ICommand {

    private int row1;
    private int row2;
    private Object element;

    public MultiplyRowWithElementAndAddToRowAndStoreCommand(int row1, int row2, Object element) {
        this.row1 = row1;
        this.row2 = row2;
        this.element = element;
    }

    @Override
    public MatrixCell[] execute(MatrixHandler handler) throws Exception {
        MatrixCell[] rowCells = handler.multipleRowWithElement(row1, element);
        handler.addRows(row2, rowCells);
        return rowCells;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " " + row1 + " " + row2 + " " + element.toString();
    }

    @Override
    public boolean affectsRows() {
        return true;
    }

    @Override
    public boolean affectsColumns() {
        return false;
    }

    @Override
    public ICommand copy() {
        return new MultiplyRowWithElementAndAddToRowAndStoreCommand(row1, row2, element);
    }

    public int getRow1() {
        return row1;
    }
    public int getRow2() {
        return row2;
    }

    public Object getElement() {
        return element;
    }
}
