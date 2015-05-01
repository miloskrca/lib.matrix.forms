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
public class MultiplyColumnWithElementAndAddToColumnAndStoreCommand extends AbstractCommand {

    private int column1;
    private int column2;
    private Object element;

    public MultiplyColumnWithElementAndAddToColumnAndStoreCommand(int column1, int column2, Object element) {
        this.column1 = column1;
        this.column2 = column2;
        this.element = element;
    }

    @Override
    protected Object executeCommand(MatrixHandler handler) throws Exception {
        MatrixCell[] columnCells = handler.multipleColumnWithElement(column1, element);
        handler.addColumns(column2, columnCells);
        return columnCells;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " " + column1 + " " + column2 + " " + element.toString();
    }

    @Override
    public boolean affectsRows() {
        return false;
    }

    @Override
    public boolean affectsColumns() {
        return true;
    }

    @Override
    public ICommand copy() {
        return new MultiplyColumnWithElementAndAddToColumnAndStoreCommand(column1, column2, element);
    }

    public int getColumn2() {
        return column2;
    }

    public int getColumn1() {
        return column1;
    }

    public Object getElement() {
        return element;
    }
}
