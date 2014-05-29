package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observers.Event.FormEvent;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class SmithIIMatrixForm extends MatrixForm {

    public SmithIIMatrixForm(MatrixHandler handler) {
        super(handler);
    }

    @Override
    public void process() throws Exception{
        sendUpdate(FormEvent.PROCESSING_STATUS);

        MatrixHandler handler = this.getHandler();
        int matrixSize = handler.getMatrix().getRowNumber();

        // TODO: finish the first part of the algorithm
        for (int range = 0; range < matrixSize - 2; range++) {
            MatrixCell smallestElement = findCellWithSmallestElement(range);
            moveElementToStartPosition(range);
            // make all elements, except for the first one, in the left outmost column equal to 0
            for (int nextRow = range + 1; nextRow < matrixSize - 1; nextRow++) {
                MatrixCell nextElement = handler.getMatrix().get(range, nextRow);
                MatrixCell quotient = calculateQuotientForElement(smallestElement, nextElement);
                MatrixCell negativeQuotient = calculateNegativeElement(quotient);
                multiplyRowWithElementAndAddToRow(range, negativeQuotient, nextRow);
            }
            // make all elements, except for the first one, in the top outmost row equal to 0
            for (int nextColumn = range + 1; nextColumn < matrixSize - 1; nextColumn++) {
                MatrixCell nextElement = handler.getMatrix().get(nextColumn, range);
                MatrixCell quotient = calculateQuotientForElement(smallestElement, nextElement);
                MatrixCell negativeQuotient = calculateNegativeElement(quotient);
                multiplyColumnWithElementAndAddToColumn(range, negativeQuotient, nextColumn);
            }
        }

        // TODO: finish the second part of the algorithm

    }

    private void moveElementToStartPosition(int range) {
        // TODO: keep in mind that basic transformations should be stored as a command
    }

    private void multiplyRowWithElementAndAddToRow(int row1, MatrixCell element, int row2) {
        // TODO: keep in mind that basic transformations should be stored as a command
    }

    private void multiplyColumnWithElementAndAddToColumn(int column1, MatrixCell element, int column2) {
        // TODO: keep in mind that basic transformations should be stored as a command
    }

    private MatrixCell calculateQuotientForElement(MatrixCell dividend, MatrixCell divisor) {
        return null;
    }

    private MatrixCell calculateNegativeElement(MatrixCell quotient) {
        return null;
    }

    private MatrixCell findCellWithSmallestElement(int range) {
        return null;
    }
}