package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Command.MatrixCommand.SwitchColumnsCommand;
import rs.etf.km123247m.Command.MatrixCommand.SwitchRowsCommand;
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
                    MatrixCell smallestElement = findCellWithSmallestElement(range);
                    moveElementToStartPosition(range, smallestElement);
                    // make all elements, except for the first one, in the left outmost column equal to 0
                    for (int nextRow = range + 1; nextRow < matrixSize - 1; nextRow++) {
                        MatrixCell nextElement = handler.getMatrix().get(range, nextRow);
                        Polynomial element = (Polynomial) nextElement.getElement();
                        if (element.compareTo(Polynomial.getZeroPolynomial()) != 0) {
                            MatrixCell quotient = calculateQuotientForElement(smallestElement, nextElement);
                            MatrixCell negativeQuotient = calculateNegativeElement(quotient);
                            multiplyRowWithElementAndAddToRow(range, negativeQuotient, nextRow);
                        }
                    }

                } while (!isColumnCleared(range));

                MatrixCell firstElement = handler.getMatrix().get(0, 0);
                // make all elements, except for the first one, in the top outmost row equal to 0 or smaller degree that first element
                for (int nextColumn = range + 1; nextColumn < matrixSize - 1; nextColumn++) {
                    MatrixCell nextElement = handler.getMatrix().get(nextColumn, range);
                    Polynomial element = (Polynomial) nextElement.getElement();
                    if (element.compareTo(Polynomial.getZeroPolynomial()) != 0) {
                        MatrixCell quotient = calculateQuotientForElement(firstElement, nextElement);
                        MatrixCell negativeQuotient = calculateNegativeElement(quotient);
                        multiplyColumnWithElementAndAddToColumn(range, negativeQuotient, nextColumn);
                    }
                }
            } while (!isRowCleared(range));
        }


        // TODO: finish the second part of the algorithm

    }

    private void moveElementToStartPosition(int range, MatrixCell element) throws Exception {
        if(element.getRow() == 0 && element.getColumn() == 0) {
            return;
        }
        if(element.getColumn() != 0) {
            ICommand command = new SwitchColumnsCommand(getHandler(), 0, element.getColumn());
            command.execute();
            getCommands().add(command);
        }
        if (element.getRow() != 0) {
            ICommand command = new SwitchRowsCommand(getHandler(), 0, element.getRow());
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

    private void multiplyRowWithElementAndAddToRow(int row1, MatrixCell element, int row2) throws Exception {
        // TODO: keep in mind that basic transformations should be stored as a command
        throw new Exception("multiplyRowWithElementAndAddToRow is not implemented!");
    }

    private void multiplyColumnWithElementAndAddToColumn(int column1, MatrixCell element, int column2) throws Exception {
        // TODO: keep in mind that basic transformations should be stored as a command
        throw new Exception("multiplyColumnWithElementAndAddToColumn is not implemented!");
    }

    private MatrixCell calculateQuotientForElement(MatrixCell dividend, MatrixCell divisor) throws Exception {
        throw new Exception("calculateQuotientForElement is not implemented!");
    }

    private MatrixCell calculateNegativeElement(MatrixCell quotient) throws Exception {
        throw new Exception("calculateNegativeElement is not implemented!");
    }

    private MatrixCell findCellWithSmallestElement(int range) throws Exception {
        throw new Exception("findCellWithSmallestElement is not implemented!");
    }
}