package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * Jun 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class DivideRowWithElementCommand implements ICommand {
    private final MatrixHandler handler;
    private final int row;
    private final Object element;

    public DivideRowWithElementCommand(MatrixHandler handler, int row, Object element) {
        this.handler = handler;
        this.row = row;
        this.element = element;
    }

    @Override
    public Object execute() throws Exception {
        return handler.divideRowWithElement(row, element);
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
