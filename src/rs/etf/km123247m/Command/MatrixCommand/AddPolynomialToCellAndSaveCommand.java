package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Polynomial.Polynomial;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class AddPolynomialToCellAndSaveCommand implements ICommand {
    private final MatrixHandler handler;
    private final Polynomial partial;
    private final int row;
    private final int column;

    public AddPolynomialToCellAndSaveCommand(MatrixHandler handler, Polynomial partial, int row, int column) {
        this.handler = handler;
        this.partial = partial;
        this.row = row;
        this.column = column;
    }

    @Override
    public Object execute() {
        return null;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public Polynomial getPartial() {
        return partial;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
