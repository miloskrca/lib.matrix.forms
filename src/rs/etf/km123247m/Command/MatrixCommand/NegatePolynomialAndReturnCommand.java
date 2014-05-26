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
public class NegatePolynomialAndReturnCommand implements ICommand {
    private final Polynomial polynomial;
    private final MatrixHandler handler;

    public NegatePolynomialAndReturnCommand(MatrixHandler handler, Polynomial polynomial) {
        this.handler = handler;
        this.polynomial = polynomial;
    }

    @Override
    public Object execute() {
        return null;
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    public MatrixHandler getHandler() {
        return handler;
    }
}
