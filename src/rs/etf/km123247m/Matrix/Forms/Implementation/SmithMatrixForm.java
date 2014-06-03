package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Command.MatrixCommand.*;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observers.Event.FormEvent;
import rs.etf.km123247m.Polynomial.Polynomial;

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
        sendUpdate(FormEvent.PROCESSING_STATUS, null);

        MatrixHandler handler = this.getHandler();
        int matrixSize = handler.getMatrix().getRowNumber();

        for (int range = 0; range < matrixSize - 1; range++) {
            do {
                do {
                    // Moving smallest to start...
                    MatrixCell smallestCell = findCellWithSmallestElement(range);
                    moveCellToStartPosition(range, smallestCell);
                    // make all elements, except for the first one, in the left outmost column equal to 0
                    for (int nextRow = range + 1; nextRow < matrixSize - 1; nextRow++) {
                        MatrixCell nextCell = handler.getMatrix().get(range, nextRow);
                        Object element = nextCell.getElement();
                        if (getHandler().compareElements(element, getHandler().getZeroElement()) != 0) {
                            MatrixCell quotient = calculateQuotientForCell(smallestCell, nextCell);
                            MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                            multiplyRowWithCellAndAddToRow(range, negativeQuotient, nextRow);
                        }
                    }

                } while (!isColumnCleared(range));

                MatrixCell firstCell = handler.getMatrix().get(0, 0);
                // make all elements, except for the first one, in the top outmost row equal to 0 or smaller degree that first element
                for (int nextColumn = range + 1; nextColumn < matrixSize - 1; nextColumn++) {
                    MatrixCell nextCell = handler.getMatrix().get(nextColumn, range);
                    Object element = nextCell.getElement();
                    if (getHandler().compareElements(element, getHandler().getZeroElement()) != 0) {
                        MatrixCell quotient = calculateQuotientForCell(firstCell, nextCell);
                        MatrixCell negativeQuotient = calculateNegativeCell(quotient);
                        multiplyColumnWithCellAndAddToColumn(range, negativeQuotient, nextColumn);
                    }
                }
            } while (!isRowCleared(range));
        }


        // TODO: finish the second part of the algorithm

    }

    private void moveCellToStartPosition(int range, MatrixCell element) throws Exception {
        if(element.getRow() == 0 && element.getColumn() == 0) {
            return;
        }
        if(element.getColumn() != 0) {
            ICommand command = new SwitchColumnsCommand(getHandler(), range, element.getColumn());
            command.execute();
            getCommands().add(command);
        }
        if (element.getRow() != 0) {
            ICommand command = new SwitchRowsCommand(getHandler(), range, element.getRow());
            command.execute();
            getCommands().add(command);
        }
    }

    private boolean isColumnCleared(int range) throws Exception {
        boolean cleared = true;
        IMatrix matrix = getHandler().getMatrix();
        for (int row = range; row < matrix.getRowNumber(); row++) {
            Polynomial element = (Polynomial) matrix.get(range, row).getElement();
            if(element.compareTo(Polynomial.getZeroPolynomial()) != 0) {
                cleared = false;
            }
        }
        return cleared;
    }

    private boolean isRowCleared(int range) throws Exception {
        boolean cleared = true;
        IMatrix matrix = getHandler().getMatrix();
        for (int column = range; column < matrix.getRowNumber(); column++) {
            Polynomial element = (Polynomial) matrix.get(column, range).getElement();
            if(element.compareTo(Polynomial.getZeroPolynomial()) != 0) {
                cleared = false;
            }
        }
        return cleared;
    }

    private void multiplyRowWithCellAndAddToRow(int row1, MatrixCell element, int row2) throws Exception {
        ICommand multiplyRowWithElementCommand = new MultiplyRowWithElementCommand(getHandler(), row1, element.getElement());
        multiplyRowWithElementCommand.execute();
        getCommands().add(multiplyRowWithElementCommand);

        ICommand addRowsCommand = new AddRowsCommand(getHandler(), row2, row1);
        addRowsCommand.execute();
        getCommands().add(addRowsCommand);
    }

    private void multiplyColumnWithCellAndAddToColumn(int column1, MatrixCell element, int column2) throws Exception {
        ICommand multiplyColumnWithElementCommand = new MultiplyColumnWithElementCommand(getHandler(), column1, element.getElement());
        multiplyColumnWithElementCommand.execute();
        getCommands().add(multiplyColumnWithElementCommand);

        ICommand addColumnsCommand = new AddColumnsCommand(getHandler(), column2, column1);
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
        Object smallestElement = getHandler().getZeroElement();
        int foundRow = -1;
        int foundColumn = -1;
        IMatrix matrix = getHandler().getMatrix();
        for (int row = range; row < matrix.getRowNumber(); row++) {
            for (int column = range; column < matrix.getColumnNumber(); column++) {
                Object element = matrix.get(row, column).getElement();
                if(getHandler().compareElements(element, smallestElement) == -1) {
                    foundRow = row;
                    foundColumn = column;
                    smallestElement = element;
                }
            }
        }
        return new MatrixCell(foundRow, foundColumn, smallestElement);
    }
}