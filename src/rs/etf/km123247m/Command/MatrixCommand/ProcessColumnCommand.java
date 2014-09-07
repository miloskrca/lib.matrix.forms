package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Created by Miloš Krsmanović.
 * Sep 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class ProcessColumnCommand implements ICommand {

    private int range;
    private int nextColumn;

    public ProcessColumnCommand(int range, int nextColumn) {
        this.range = range;
        this.nextColumn = nextColumn;
    }

    @Override
    public Object execute(MatrixHandler handler) throws Exception {
        MatrixCell firstCell = handler.getMatrix().get(range, range);
        MatrixCell nextCell = handler.getMatrix().get(range, nextColumn);

        Object quotient = handler.divideCellElements(nextCell.getElement(), firstCell.getElement());
        Object nObject = handler.calculateNegativeElement(quotient);

        MatrixCell[] columnCells = handler.multipleColumnWithElement(range, nObject);
        handler.addColumns(nextColumn, columnCells);
        return columnCells;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " | Range: " + range + " | NextColumn: " + nextColumn;
    }

    @Override
    public boolean affectsRows() {
        return false;
    }

    @Override
    public boolean affectsColumns() {
        return true;
    }

    public int getRange() {
        return range;
    }

    public int getNextColumn() {
        return nextColumn;
    }
}
