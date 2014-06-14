package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Command.MatrixCommand.*;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observers.Event.FormEvent;

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
                    MatrixCell smallestCell = findCellWithSmallestElement(range);
                    moveCellToStartPosition(range, smallestCell);
                    sendUpdate(FormEvent.PROCESSING_STATUS, null);
                    // make all elements, except for the first one, in the left outmost column equal to 0
                    for (int nextRow = range + 1; nextRow < matrixSize; nextRow++) {
                        MatrixCell nextCell = handler.getMatrix().get(nextRow, range);
                        Object element = nextCell.getElement();
                        if (getHandler().compareElements(element, getHandler().getZeroElement()) != 0) {
                            MatrixCell quotient = calculateQuotientForCell(nextCell, smallestCell);
                            MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                            multiplyRowWithCellAndAddToRow(range, negativeQuotient, nextRow);
                            sendUpdate(FormEvent.PROCESSING_STATUS, null);
                        }
                    }

                } while (!isColumnCleared(range));

                MatrixCell firstCell = handler.getMatrix().get(range, range);
                // make all elements, except for the first one, in the top outmost row equal to 0 or smaller degree that first element
                for (int nextColumn = range + 1; nextColumn < matrixSize; nextColumn++) {
                    MatrixCell nextCell = handler.getMatrix().get(range, nextColumn);
                    Object element = nextCell.getElement();
                    if (getHandler().compareElements(element, getHandler().getZeroElement()) != 0) {
                        MatrixCell quotient = calculateQuotientForCell(nextCell, firstCell);
                        MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                        multiplyColumnWithCellAndAddToColumn(range, negativeQuotient, nextColumn);
                        sendUpdate(FormEvent.PROCESSING_STATUS, null);
                    }
                }
            } while (!isRowCleared(range));
        }


        // TODO: finish the second part of the algorithm

    }

    private void moveCellToStartPosition(int range, MatrixCell element) throws Exception {
        if(element.getRow() == range && element.getColumn() == range) {
            return;
        }
        if(element.getColumn() != range) {
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

    private boolean isColumnCleared(int range) throws Exception {
        boolean cleared = true;
        IMatrix matrix = getHandler().getMatrix();
        for (int row = range + 1; row < matrix.getRowNumber(); row++) {
            Object element = matrix.get(row, range).getElement();
            if(getHandler().compareElements(element, getHandler().getZeroElement()) != 0) {
                cleared = false;
            }
        }
        return cleared;
    }

    private boolean isRowCleared(int range) throws Exception {
        boolean cleared = true;
        IMatrix matrix = getHandler().getMatrix();
        for (int column = range + 1; column < matrix.getRowNumber(); column++) {
            Object element = matrix.get(range, column).getElement();
            if(getHandler().compareElements(element, getHandler().getZeroElement()) != 0) {
                cleared = false;
            }
        }
        return cleared;
    }

    private void multiplyRowWithCellAndAddToRow(int row1, MatrixCell cell, int row2) throws Exception {
        ICommand multiplyRowWithElementCommand = new MultiplyRowWithElementCommand(getHandler(), row1, cell.getElement());
        MatrixCell[] resultRow = (MatrixCell[]) multiplyRowWithElementCommand.execute();
        getCommands().add(multiplyRowWithElementCommand);

        ICommand addRowsCommand = new AddRowsCommand(getHandler(), row2, resultRow);
        addRowsCommand.execute();
        getCommands().add(addRowsCommand);
    }

    private void multiplyColumnWithCellAndAddToColumn(int column1, MatrixCell element, int column2) throws Exception {
        ICommand multiplyColumnWithElementCommand = new MultiplyColumnWithElementCommand(getHandler(), column1, element.getElement());
        MatrixCell[] resultColumn = (MatrixCell[]) multiplyColumnWithElementCommand.execute();
        getCommands().add(multiplyColumnWithElementCommand);

        ICommand addColumnsCommand = new AddColumnsCommand(getHandler(), column2, resultColumn);
        addColumnsCommand.execute();
        getCommands().add(addColumnsCommand);
    }

    private MatrixCell calculateQuotientForCell(MatrixCell dividend, MatrixCell divisor) throws Exception {
        Object object = getHandler().divideCellElements(dividend.getElement(), divisor.getElement());
        return new MatrixCell(dividend.getRow(), dividend.getColumn(), object);
    }

    private MatrixCell calculateNegativeCell(MatrixCell cell) throws Exception {
        Object object = getHandler().calculateNegativeElement(cell.getElement());
        return new MatrixCell(cell.getRow(), cell.getColumn(), object);
    }

    private MatrixCell findCellWithSmallestElement(int range) throws Exception {
        MatrixCell smallestCell = getHandler().getMatrix().get(range, range);
        IMatrix matrix = getHandler().getMatrix();
        int rowNumber = matrix.getRowNumber();
        for (int row = range; row < rowNumber; row++) {
            for (int column = range; column < matrix.getColumnNumber(); column++) {
                if(row == range && column == range) {
                    continue;
                }
                MatrixCell cell = matrix.get(row, column);
                if(getHandler().compareElements(cell.getElement(), smallestCell.getElement()) == -1) {
                    smallestCell = cell;
                }
            }
        }
        return smallestCell;
    }
}