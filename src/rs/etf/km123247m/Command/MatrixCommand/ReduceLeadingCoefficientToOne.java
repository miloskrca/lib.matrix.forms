package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * Jun 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class ReduceLeadingCoefficientToOne implements ICommand {
    private MatrixHandler handler;

    public ReduceLeadingCoefficientToOne(MatrixHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object execute() throws Exception {
        ((PolynomialMatrixHandler)handler).reduceLeadingCoefficientToOne();
        return null;
    }
}
