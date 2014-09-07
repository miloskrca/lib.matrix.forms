package rs.etf.km123247m.Command.MatrixCommand;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Created by Miloš Krsmanović.
 * Sep 2014
 * <p/>
 * package: rs.etf.km123247m.Command.MatrixCommand
 */
public class FixLeadingCoefficientCommand implements ICommand {

    private int range;

    public FixLeadingCoefficientCommand(int range) {
        this.range = range;
    }

    @Override
    public Object execute(MatrixHandler handler) throws Exception {
        Object leadingCoefficient = ((PolynomialMatrixHandler)handler).getLeadingCoefficient(
                handler.getMatrix().get(range, range).getElement()
        );
        Object invertedCoefficient = ((PolynomialMatrixHandler)handler).getInverse(leadingCoefficient);
        MatrixCell[] rowCells = handler.multipleRowWithElement(range, invertedCoefficient);
        handler.storeRow(range, rowCells);
        return rowCells;
    }

    @Override
    public String getDescription() {
        return this.getClass().getSimpleName() + " | Range: " + range;
    }

    @Override
    public boolean affectsRows() {
        return true;
    }

    @Override
    public boolean affectsColumns() {
        return false;
    }

    public int getRange() {
        return range;
    }

}
