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
public class MultiplyPolynomialWithCellAndReturnCommand implements ICommand {
    private final MatrixHandler handler;
    private final Polynomial polynomial;
    private final int row;
    private final int column;

    public MultiplyPolynomialWithCellAndReturnCommand(MatrixHandler handler, Polynomial polynomial, int row, int column) {
        this.handler = handler;
        this.polynomial = polynomial;
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }
}
