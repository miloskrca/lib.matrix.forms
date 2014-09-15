package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Stores in second.
 *
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class AddColumnsAndStoreCommand implements ICommand {

    private int column1;
    private int column2;

    public AddColumnsAndStoreCommand(int column1, int column2) {
        this.column1 = column1;
        this.column2 = column2;
    }

    @Override
    public MatrixCell[] execute(MatrixHandler handler) throws Exception {
        //Stores in second.
        handler.addColumns(column1, column2);
        return null;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " Column1: " + column1 + " Column2: " + column2;
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
        return new AddColumnsAndStoreCommand(column1, column2);
    }

    public int getColumn1() {
        return column1;
    }
    public int getColumn2() {
        return column2;
    }

}
