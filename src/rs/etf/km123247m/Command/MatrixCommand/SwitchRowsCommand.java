package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class SwitchRowsCommand implements ICommand {

    private int row1;
    private int row2;

    public SwitchRowsCommand(int row1, int row2) {
        this.row1 = row1;
        this.row2 = row2;
    }

    @Override
    public Object execute(MatrixHandler handler) throws Exception {
        handler.switchRows(row1, row2);
        return null;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " " + row1 + " " + row2;
    }

    @Override
    public boolean affectsRows() {
        return true;
    }

    @Override
    public boolean affectsColumns() {
        return false;
    }

    public int getRow1() {
        return row1;
    }

    public int getRow2() {
        return row2;
    }
}
