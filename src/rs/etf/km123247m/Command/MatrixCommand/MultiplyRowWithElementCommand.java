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
    public MatrixCell[] execute() throws Exception {
        return handler.multipleRowWithElement(row, element);
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