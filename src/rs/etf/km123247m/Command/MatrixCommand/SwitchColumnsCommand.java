package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class SwitchColumnsCommand extends AbstractCommand {

    private int column1;
    private int column2;

    public SwitchColumnsCommand(int column1, int column2) {
        this.column1 = column1;
        this.column2 = column2;
    }

    @Override
    protected Object executeCommand(MatrixHandler handler) throws Exception {
        handler.switchColumns(column1, column2);
        return null;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " " + column1 + " " + column2;
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
        return new SwitchColumnsCommand(column1, column2);
    }

    public int getColumn1() {
        return column1;
    }

    public int getColumn2() {
        return column2;
    }
}
