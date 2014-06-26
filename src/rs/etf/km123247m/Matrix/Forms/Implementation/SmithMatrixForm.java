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
public class SmithMatrixForm extends MatrixForm {

    public SmithMatrixForm(MatrixHandler handler) {
        super(handler);
    }

    @Override
    public void process() throws Exception {
        MatrixHandler handler = this.getHandler();
        int matrixSize = handler.getMatrix().getRowNumber();

        for (int range = 0; range < matrixSize - 1; range++) {
            do {
                do {
                    // Moving smallest to start...
                    MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, matrixSize);
                    moveCellToStartPosition(range, smallestCell);
                    sendUpdate(FormEvent.PROCESSING_STATUS, null);
                    // make all elements, except for the first one, in the left outmost column equal to 0
                    for (int nextRow = range + 1; nextRow < matrixSize; nextRow++) {
                        MatrixCell nextCell = handler.getMatrix().get(nextRow, range);
                        if (!getHandler().isZeroElement(nextCell.getElement())) {
                            MatrixCell quotient = calculateQuotientForCell(nextCell, smallestCell);
                            MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                            multiplyRowWithCellAndAddToRow(range, negativeQuotient, nextRow);
                            sendUpdate(FormEvent.PROCESSING_STATUS, null);
                        }
                    }

                } while (!isColumnCleared(range));

                MatrixCell firstCell = handler.getMatrix().get(range, range);
                // make all elements, except for the first one, in the top outmost row equal to 0 or smaller power that first element
                for (int nextColumn = range + 1; nextColumn < matrixSize; nextColumn++) {
                    MatrixCell nextCell = handler.getMatrix().get(range, nextColumn);
                    if (!getHandler().isZeroElement(nextCell.getElement())) {
                        MatrixCell quotient = calculateQuotientForCell(nextCell, firstCell);
                        MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                        multiplyColumnWithCellAndAddToColumn(range, negativeQuotient, nextColumn);
                        sendUpdate(FormEvent.PROCESSING_STATUS, null);
                    }
                }
            } while (!isRowCleared(range));
        }

        if (!allElementsOnTheDiagonalAreDividingTheNextElement()) {
            for (int range = 0; range < matrixSize - 1; range++) {
                if (isPowerGreaterThenPowerOnNextElement(range)) {
                    addTwoRows(range, range + 1);
                    sendUpdate(FormEvent.PROCESSING_STATUS, null);

                    int tempMatrixSize = range + 2;
                    do {
                        do {
                            // Moving smallest to start...
                            MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, tempMatrixSize);
                            moveCellToStartPosition(range, smallestCell);
                            sendUpdate(FormEvent.PROCESSING_STATUS, null);

                            // make all elements, except for the first one, in the top outmost row equal to 0 or smaller power that first element
                            for (int nextColumn = range + 1; nextColumn < tempMatrixSize; nextColumn++) {
                                MatrixCell nextCell = handler.getMatrix().get(range, nextColumn);
                                if (!getHandler().isZeroElement(nextCell.getElement())) {
                                    MatrixCell quotient = calculateQuotientForCell(nextCell, smallestCell);
                                    MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                                    multiplyColumnWithCellAndAddToColumn(range, negativeQuotient, nextColumn);
                                    sendUpdate(FormEvent.PROCESSING_STATUS, null);
                                }
                            }
                        } while (!isRowCleared(range));

                        MatrixCell firstCell = handler.getMatrix().get(range, range);
                        for (int nextRow = range + 1; nextRow < tempMatrixSize; nextRow++) {
                            MatrixCell nextCell = handler.getMatrix().get(nextRow, range);
                            if (!getHandler().isZeroElement(nextCell.getElement())) {
                                MatrixCell quotient = calculateQuotientForCell(nextCell, firstCell);
                                MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                                multiplyRowWithCellAndAddToRow(range, negativeQuotient, nextRow);
                                sendUpdate(FormEvent.PROCESSING_STATUS, null);
                            }
                        }
                    } while (!isColumnCleared(range));
                }
            }
        }
        fixLeadingCoefficientOfFirstElement();
    }

    private void fixLeadingCoefficientOfFirstElement() throws Exception {
        Object leadingCoefficient = ((PolynomialMatrixHandler)getHandler()).getLeadingCoefficient(getHandler().getMatrix().get(0, 0));
        ICommand divideRowWithElementAndStoreCommand = new DivideRowWithElementAndStoreCommand(getHandler(), 0, leadingCoefficient);
        /*MatrixCell[] resultRow = (MatrixCell[]) */divideRowWithElementAndStoreCommand.execute();
        getCommands().add(divideRowWithElementAndStoreCommand);
    }

    protected void moveCellToStartPosition(int range, MatrixCell element) throws Exception {
        if (element.getRow() == range && element.getColumn() == range) {
            return;
        }
        if (element.getColumn() != range) {
            ICommand command = new SwitchColumnsCommand(getHandler(), range, element.getColumn());
            command.execute();
            getCommands().add(command);
        }
        if (element.getRow() != range) {
            ICommand command = new SwitchRowsCommand(getHandler(), range, element.getRow());
            command.execute();
            getCommands().add(command);
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

    protected void multiplyRowWithCellAndAddToRow(int row1, MatrixCell cell, int row2) throws Exception {
        // TODO: This all should be a single command so it can be used with single matrix
        ICommand multiplyRowWithElementCommand = new MultiplyRowWithElementCommand(getHandler(), row1, cell.getElement());
        MatrixCell[] resultRow = (MatrixCell[]) multiplyRowWithElementCommand.execute();
        getCommands().add(multiplyRowWithElementCommand);

        ICommand addRowOfObjectsToRowCommand = new AddRowOfCellsToRowCommand(getHandler(), row2, resultRow);
        addRowOfObjectsToRowCommand.execute();
        getCommands().add(addRowOfObjectsToRowCommand);
    }

    protected void multiplyColumnWithCellAndAddToColumn(int column1, MatrixCell element, int column2) throws Exception {
        // TODO: This all should be a single command so it can be used with single matrix
        ICommand multiplyColumnWithElementCommand = new MultiplyColumnWithElementCommand(getHandler(), column1, element.getElement());
        MatrixCell[] resultColumn = (MatrixCell[]) multiplyColumnWithElementCommand.execute();
        getCommands().add(multiplyColumnWithElementCommand);

        ICommand addColumnsCommand = new AddColumnOfCellsToColumnCommand(getHandler(), column2, resultColumn);
        addColumnsCommand.execute();
        getCommands().add(addColumnsCommand);
    }

    protected MatrixCell calculateQuotientForCell(MatrixCell dividend, MatrixCell divisor) throws Exception {
        Object object = getHandler().divideCellElements(dividend.getElement(), divisor.getElement());
        return new MatrixCell(dividend.getRow(), dividend.getColumn(), object);
    }

    protected MatrixCell calculateNegativeCell(MatrixCell cell) throws Exception {
        Object object = getHandler().calculateNegativeElement(cell.getElement());
        return new MatrixCell(cell.getRow(), cell.getColumn(), object);
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
                if (getHandler().comparePowersOfElements(cell.getElement(), smallestCell.getElement()) == -1) {
                    smallestCell = cell;
                }
            }
        }
        return smallestCell;
    }

    protected boolean allElementsOnTheDiagonalAreDividingTheNextElement() throws Exception {
        boolean allClear = true;
        IMatrix matrix = getHandler().getMatrix();
        MatrixCell lastCell = matrix.get(0, 0);
        int rowNumber = matrix.getRowNumber();
        int columnNumber = matrix.getColumnNumber();
        for (int row = 1; row < rowNumber; row++) {
            if (!allClear) {
                break;
            }
            for (int column = 1; column < columnNumber; column++) {
                if (row != column) {
                    continue;
                }
                MatrixCell cell = matrix.get(row, column);
                Object remainder = getHandler().divideCellElementsAndReturnRemainder(cell.getElement(), lastCell.getElement());
                if (!getHandler().isZeroElement(remainder)) {
                    allClear = false;
                    break;
                }
                lastCell = cell;
            }
        }
        return allClear;
    }

    protected boolean isPowerGreaterThenPowerOnNextElement(int range) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        MatrixCell cell1 = handler.getMatrix().get(range, range);
        MatrixCell cell2 = handler.getMatrix().get(range + 1, range + 1);
        return handler.getPower(cell1) > handler.getPower(cell2);
    }

    protected void addTwoRows(int row1, int row2) throws Exception {
        ICommand addRowsCommand = new AddRowsCommand(getHandler(), row1, row2);
        addRowsCommand.execute();
        getCommands().add(addRowsCommand);
    }

}