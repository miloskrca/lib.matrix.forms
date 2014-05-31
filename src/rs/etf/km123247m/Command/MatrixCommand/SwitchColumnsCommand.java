package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class SwitchColumnsCommand implements ICommand {

    private MatrixHandler handler;
    private int column1;
    private int column2;

    public SwitchColumnsCommand(MatrixHandler handler, int column1, int column2) {
        this.handler = handler;
        this.column1 = column1;
        this.column2 = column2;
    }

    @Override
    public Object execute() throws Exception {
        handler.switchColumns(column1, column2);
        return null;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public int getColumn1() {
        return column1;
    }

    public int getColumn2() {
        return column2;
    }
}
