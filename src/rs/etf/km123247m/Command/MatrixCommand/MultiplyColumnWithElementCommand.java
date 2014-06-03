package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class MultiplyColumnWithElementCommand implements ICommand {

    private MatrixHandler handler;
    private int column;
    private Object element;

    public MultiplyColumnWithElementCommand(MatrixHandler handler, int column, Object element) {
        this.handler = handler;
        this.column = column;
        this.element = element;
    }

    @Override
    public Object execute() throws Exception {
        handler.multipleColumnWithObject(column, element);
        return null;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public int getColumn() {
        return column;
    }

    public Object getElement() {
        return element;
    }
}
