package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Command.MatrixCommand.*;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observer.Event.FormEvent;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class SmithMatrixForm2 extends MatrixForm {

    public SmithMatrixForm2(MatrixHandler handler) {
        super(handler);
    }

    @Override
    public void process() throws Exception {
        MatrixHandler handler = this.getHandler();
        int matrixSize = handler.getMatrix().getRowNumber();

        for (int range = 0; range < matrixSize - 1; range++) {
            while (!isRowCleared(range) || !isColumnCleared(range)) {
                while (!isColumnCleared(range)) {
                    // Moving smallest to start...
                    MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, matrixSize);
                    moveCellToStartPosition(range, smallestCell);
                    // make all elements, except for the first one, in the left outmost column equal to 0
                    for (int nextRow = range + 1; nextRow < matrixSize; nextRow++) {
                        MatrixCell nextCell = handler.getMatrix().get(nextRow, range);
                        if (!getHandler().isZeroElement(nextCell.getElement())) {
                            executeCommand(new ProcessRowCommand(range, nextRow));
                        }
                    }
                } // } while (!isColumnCleared(range));

                // Moving smallest to start...
                MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, matrixSize);
                moveCellToStartPosition(range, smallestCell);
                // make all elements, except for the first one, in the top outmost row equal to 0 or smaller power that first element
                for (int nextColumn = range + 1; nextColumn < matrixSize; nextColumn++) {
                    MatrixCell nextCell = handler.getMatrix().get(range, nextColumn);
                    if (!getHandler().isZeroElement(nextCell.getElement())) {
                        executeCommand(new ProcessColumnCommand(range, nextColumn));
                    }
                }
            } // } while (!isRowCleared(range));
        }

        sendUpdate(FormEvent.PROCESSING_INFO, "Fixing elements on the diagonal", getHandler().getMatrix());

        for (int range = 0; range < matrixSize - 1; range++) {
            if (!isTheNextElementDividedByThisElement(range)) {
                addTwoRows(range + 1, range); //stores in second

                int tempMatrixSize = range + 2;
                while (!isColumnCleared(range) || !isRowCleared(range)) {
                    while (!isRowCleared(range)) {
                        // Moving smallest to start...
                        MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, tempMatrixSize);
                        moveCellToStartPosition(range, smallestCell);
                        // make all elements, except for the first one, in the top outmost row equal to 0 or smaller power that first element
                        for (int nextColumn = range + 1; nextColumn < tempMatrixSize; nextColumn++) {
                            MatrixCell nextCell = handler.getMatrix().get(range, nextColumn);
                            if (!getHandler().isZeroElement(nextCell.getElement())) {
                                executeCommand(new ProcessColumnCommand(range, nextColumn));
                            }
                        }
                    } // while (!isRowCleared(range));

                    // Moving smallest to start...
                    MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, tempMatrixSize);
                    moveCellToStartPosition(range, smallestCell);
                    for (int nextRow = range + 1; nextRow < tempMatrixSize; nextRow++) {
                        MatrixCell nextCell = handler.getMatrix().get(nextRow, range);
                        if (!getHandler().isZeroElement(nextCell.getElement())) {
                            executeCommand(new ProcessRowCommand(range, nextRow));
                        }
                    }
                }//  while (!isColumnCleared(range));
            }
        }

        fixLeadingCoefficients();
    }

    protected void executeCommand(ICommand command) throws Exception {
        command.execute(getHandler());
        getCommands().add(command);
        sendUpdate(FormEvent.PROCESSING_STEP, command.getDescription(), getHandler().getMatrix());
    }

    protected void moveCellToStartPosition(int range, MatrixCell element) throws Exception {
        if (element.getRow() == range && element.getColumn() == range) {
            return;
        }
        if (element.getColumn() != range) {
            executeCommand(new SwitchColumnsCommand(range, element.getColumn()));
        }
        if (element.getRow() != range) {
            executeCommand(new SwitchRowsCommand(range, element.getRow()));
        }
    }

    protected boolean isColumnCleared(int range) throws Exception {
        boolean cleared = true;
        IMatrix matrix = getHandler().getMatrix();
        for (int row = range + 1; row < matrix.getRowNumber(); row++) {
            Object element = matrix.get(row, range).getElement();
            if (!getHandler().isZeroElement(element)) {
                cleared = false;
            }
        }
        return cleared;
    }

    protected boolean isRowCleared(int range) throws Exception {
        boolean cleared = true;
        IMatrix matrix = getHandler().getMatrix();
        for (int column = range + 1; column < matrix.getRowNumber(); column++) {
            Object element = matrix.get(range, column).getElement();
            if (!getHandler().isZeroElement(element)) {
                cleared = false;
            }
        }
        return cleared;
    }

    protected MatrixCell findCellWithElementWithSmallestPower(int rangeFrom, int rangeTo) throws Exception {
        IMatrix matrix = getHandler().getMatrix();
        MatrixCell smallestCell = matrix.get(rangeFrom, rangeFrom);
        for (int row = rangeFrom; row < rangeTo; row++) {
            for (int column = rangeFrom; column < rangeTo; column++) {
                if (row == rangeFrom && column == rangeFrom) {
                    continue;
                }
                MatrixCell cell = matrix.get(row, column);
                if (getHandler().comparePowersOfElements(cell.getElement(), smallestCell.getElement()) == -1
                        && !getHandler().isZeroElement(cell.getElement())) {
                    smallestCell = cell;
                }
            }
        }
        return smallestCell;
    }

    private boolean isTheNextElementDividedByThisElement(int range) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        MatrixCell cell1 = handler.getMatrix().get(range, range);
        MatrixCell cell2 = handler.getMatrix().get(range + 1, range + 1);
        return handler.isElementDividing(cell2.getElement(), cell1.getElement());
    }

    protected void addTwoRows(int row1, int row2) throws Exception {
        executeCommand(new AddRowsAndStoreCommand(row1, row2));
    }

    private void fixLeadingCoefficients() throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();

        int rowNumber = handler.getMatrix().getRowNumber();
        for (int row = 0; row < rowNumber; row++) {
            Object leadingCoefficient = handler.getLeadingCoefficient(getHandler().getMatrix().get(row, row).getElement());
            if (handler.compare(leadingCoefficient, handler.getOne()) != 0) {
                executeCommand(new FixLeadingCoefficientCommand(row));
            }
        }
    }

}