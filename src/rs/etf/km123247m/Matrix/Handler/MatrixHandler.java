package rs.etf.km123247m.Matrix.Handler;

import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Math.Matrix
 */
// TODO: Implement signatures for all necessary matrix operations.
public abstract class MatrixHandler {
    private IMatrix matrix;

    protected MatrixHandler(IMatrix matrix) {
        this.matrix = matrix;
    }

    public IMatrix getMatrix() {
        return matrix;
    }

    public void setMatrix(IMatrix matrix) {
        this.matrix = matrix;
    }

    public void switchRows(int row1, int row2) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        for (int column = 0; column < numOfColumns; column++) {
            //get cell from first row
            Object temp = matrix.get(row1, column).getElement();
            //set cell from second row to first row
            matrix.set(new MatrixCell(row1, column, matrix.get(row2, column).getElement()));
            //set cell from first row to second row
            matrix.set(new MatrixCell(row2, column, temp));
        }
    }

    public void switchColumns(int column1, int column2) throws Exception {
        int numOfRows = matrix.getRowNumber();
        for (int row = 0; row < numOfRows; row++) {
            //get cell from first column
            Object temp = matrix.get(row, column1).getElement();
            //set cell from second column to first column
            matrix.set(new MatrixCell(row, column1, matrix.get(row, column2).getElement()));
            //set cell from first column to second column
            matrix.set(new MatrixCell(row, column2, temp));
        }
    }
}