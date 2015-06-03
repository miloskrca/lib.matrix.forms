package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Command.MatrixCommand.*;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observer.Event.FormEvent;

import java.util.Arrays;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class SmithMatrixForm extends MatrixForm {

    private IMatrix startMatrix;

    private IMatrix finalMatrix;

    public SmithMatrixForm(MatrixHandler handler) {
        super(handler);
        try {
            startMatrix = handler.duplicate(handler.getMatrix());
        } catch (Exception exception) {
            sendUpdate(FormEvent.PROCESSING_EXCEPTION, exception.getMessage() + Arrays.toString(exception.getStackTrace()), handler.getMatrix());
        }
        finalMatrix = handler.getMatrix();
    }

    @Override
    public void process() throws Exception {
        sendUpdate(FormEvent.PROCESSING_START, null, getHandler().getMatrix());
        MatrixHandler handler = this.getHandler();

        if (handler.isSingular()) {
            sendUpdate(FormEvent.PROCESSING_EXCEPTION, FormEvent.EXCEPTION_MATRIX_IS_SINGULAR, null);
            return;
        }

        int matrixSize = handler.getMatrix().getRowNumber();

        for (int range = 0; range < matrixSize - 1; range++) {
            while (!isRowCleared(range) || !isColumnCleared(range)) { // do {
                while (!isColumnCleared(range)) { // do {
                    // Moving smallest to start...
                    MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, matrixSize);
                    moveCellToStartPosition(range, smallestCell);
                    // make all elements, except for the first one, in the left outmost column equal to 0
                    for (int nextRow = range + 1; nextRow < matrixSize; nextRow++) {
                        MatrixCell nextCell = handler.getMatrix().get(nextRow, range);
                        if (!getHandler().isZeroElement(nextCell.getElement())) {
                            MatrixCell quotient = calculateQuotientForCell(nextCell, smallestCell);
                            MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                            multiplyRowWithCellAndAddToRow(range, negativeQuotient, nextRow);
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
                        MatrixCell quotient = calculateQuotientForCell(nextCell, smallestCell);
                        MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                        multiplyColumnWithCellAndAddToColumn(range, negativeQuotient, nextColumn);
                    }
                }
            } // } while (!isRowCleared(range));
        }

        if (!isTheDiagonalOk()) {
            sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_FIX_ELEMENTS_ON_DIAGONAL, getHandler().getMatrix());
        }

        int numOfCommandsBeforeFixing = getCommands().size();
        while (!isTheDiagonalOk()) {
            for (int range = 0; range < matrixSize - 1; range++) {
                if (!isTheNextElementDividedByThisElement(range)) {
                    addTwoRows(range + 1, range); //stores in second

                    int tempMatrixSize = range + 2;
                    while (!isColumnCleared(range) || !isRowCleared(range)) { // do {
                        while (!isRowCleared(range)) { // do {
                            // Moving smallest to start...
                            MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, tempMatrixSize);
                            moveCellToStartPosition(range, smallestCell);
                            // make all elements, except for the first one, in the top outmost row equal to 0 or smaller power that first element
                            for (int nextColumn = range + 1; nextColumn < tempMatrixSize; nextColumn++) {
                                MatrixCell nextCell = handler.getMatrix().get(range, nextColumn);
                                if (!getHandler().isZeroElement(nextCell.getElement())) {
                                    MatrixCell quotient = calculateQuotientForCell(nextCell, smallestCell);
                                    MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                                    multiplyColumnWithCellAndAddToColumn(range, negativeQuotient, nextColumn);
                                }
                            }
                        } // while (!isRowCleared(range));

                        // Moving smallest to start...
                        MatrixCell smallestCell = findCellWithElementWithSmallestPower(range, tempMatrixSize);
                        moveCellToStartPosition(range, smallestCell);
                        for (int nextRow = range + 1; nextRow < tempMatrixSize; nextRow++) {
                            MatrixCell nextCell = handler.getMatrix().get(nextRow, range);
                            if (!getHandler().isZeroElement(nextCell.getElement())) {
                                MatrixCell quotient = calculateQuotientForCell(nextCell, smallestCell);
                                MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                                multiplyRowWithCellAndAddToRow(range, negativeQuotient, nextRow);
                            }
                        }
                    }//  while (!isColumnCleared(range));
                }
            }
        }

        if (getCommands().size() > numOfCommandsBeforeFixing) {
            sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_END_FIX_ELEMENTS_ON_DIAGONAL, getHandler().getMatrix());
        }
        sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_FIX_LEADING_COEFFICIENTS, getHandler().getMatrix());
        numOfCommandsBeforeFixing = getCommands().size();
        fixLeadingCoefficients();
        if (getCommands().size() > numOfCommandsBeforeFixing) {
            sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_END_FIX_LEADING_COEFFICIENTS, getHandler().getMatrix());
        }
        sendUpdate(FormEvent.PROCESSING_END, null, getHandler().getMatrix());
    }

    protected void moveCellToStartPosition(int range, MatrixCell element) throws Exception {
        if (element.getRow() == range && element.getColumn() == range) {
            return;
        }
        if (element.getColumn() != range) {
            ICommand command = new SwitchColumnsCommand(range, element.getColumn());
            command.execute(getHandler());
            getCommands().add(command);
            sendUpdate(FormEvent.PROCESSING_STEP, command.getDescription(), getHandler().getMatrix());
        }
        if (element.getRow() != range) {
            ICommand command = new SwitchRowsCommand(range, element.getRow());
            command.execute(getHandler());
            getCommands().add(command);
            sendUpdate(FormEvent.PROCESSING_STEP, command.getDescription(), getHandler().getMatrix());
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
        ICommand command = new MultiplyRowWithElementAndAddToRowAndStoreCommand(
                row1, row2, cell.getElement()
        );
        command.execute(getHandler());
        getCommands().add(command);
        sendUpdate(FormEvent.PROCESSING_STEP, command.getDescription(), getHandler().getMatrix());
    }

    protected void multiplyColumnWithCellAndAddToColumn(int column1, MatrixCell cell, int column2) throws Exception {
        ICommand command = new MultiplyColumnWithElementAndAddToColumnAndStoreCommand(
                column1, column2, cell.getElement()
        );
        command.execute(getHandler());
        getCommands().add(command);
        sendUpdate(FormEvent.PROCESSING_STEP, command.getDescription(), getHandler().getMatrix());
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
                if (getHandler().comparePowersOfElements(cell.getElement(), smallestCell.getElement()) == -1
                        && !getHandler().isZeroElement(cell.getElement())) {
                    smallestCell = cell;
                }

                if (getHandler().isZeroElement(smallestCell.getElement())
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


    private boolean isTheDiagonalOk() throws Exception {
        int rowNumber = getHandler().getMatrix().getRowNumber();
        for (int range = 0; range < rowNumber - 1; range++) {
            if (!isTheNextElementDividedByThisElement(range)) {
                return false;
            }
        }
        return true;
    }

    protected void addTwoRows(int row1, int row2) throws Exception {
        ICommand command = new AddRowsAndStoreCommand(
                row1, row2
        );
        command.execute(getHandler());
        getCommands().add(command);
        sendUpdate(FormEvent.PROCESSING_STEP, command.getDescription(), getHandler().getMatrix());
    }

    private void fixLeadingCoefficients() throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();

        int rowNumber = handler.getMatrix().getRowNumber();
        for (int row = 0; row < rowNumber; row++) {
            Object leadingCoefficient = handler.getLeadingCoefficient(getHandler().getMatrix().get(row, row).getElement());
            if (handler.compare(leadingCoefficient, handler.getOne()) != 0) {
                // divide row with element with its leading coefficient
                ICommand command = new MultiplyRowWithElementAndStoreCommand(row, handler.getInverse(leadingCoefficient));
                command.execute(getHandler());
                getCommands().add(command);
                sendUpdate(FormEvent.PROCESSING_STEP, command.getDescription(), getHandler().getMatrix());
            }
        }
    }

    public IMatrix getFinalMatrix() {
        return finalMatrix;
    }

    public IMatrix getStartMatrix() {
        return startMatrix;
    }
}