package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class MultiplyRowWithElementCommand implements ICommand {

    private MatrixHandler handler;
    private int row;
    private Object element;

    public MultiplyRowWithElementCommand(MatrixHandler handler, int row, Object element) {
        this.handler = handler;
        this.row = row;
        this.element = element;
    }

    @Override
    public Object execute() throws Exception {
        handler.multipleRowWithObject(row, element);
        return null;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public int getRow() {
        return row;
    }

    public Object getElement() {
        return element;
    }
}
