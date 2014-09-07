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
public class ProcessRowCommand implements ICommand {

    private int range;
    private int nextRow;

    public ProcessRowCommand(int range, int nextRow) {
        this.range = range;
        this.nextRow = nextRow;
    }

    @Override
    public Object execute(MatrixHandler handler) throws Exception {
        MatrixCell firstCell = handler.getMatrix().get(range, range);
        MatrixCell nextCell = handler.getMatrix().get(nextRow, range);

        Object quotient = handler.divideCellElements(nextCell.getElement(), firstCell.getElement());
        Object nObject = handler.calculateNegativeElement(quotient);

        MatrixCell[] rowCells = handler.multipleRowWithElement(range, nObject);
        handler.addRows(nextRow, rowCells);
        return rowCells;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " | Range: " + range + " | NextRow: " + nextRow;
    }

    @Override
    public boolean affectsRows() {
        return true;
    }

    @Override
    public boolean affectsColumns() {
        return false;
    }

    public int getRange() {
        return range;
    }

    public int getNextRow() {
        return nextRow;
    }
}
