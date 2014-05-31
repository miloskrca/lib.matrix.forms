package rs.etf.km123247m.Matrix.Implementation;

import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Created by Miloš Krsmanović.
 * 2014
 *
 * package: rs.etf.km123247m.Matrix
 */
public class ArrayMatrix implements IMatrix {
    private final int rowNumber;
    private final int columnNumber;
    private Object[][] matrix;

    public ArrayMatrix(int columnNumber, int rowNumber) {
        this.columnNumber = columnNumber;
        this.rowNumber = rowNumber;
        this.matrix = new Object[rowNumber][columnNumber];
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void set(MatrixCell cell) throws Exception {
        checkColumnAndRowValues(cell.getColumn(), cell.getRow());
        matrix[cell.getRow()][cell.getColumn()] = cell.getElement();
    }

    public MatrixCell get(int row, int col) throws Exception {
        checkColumnAndRowValues(row, col);
        return new MatrixCell(row, col, matrix[row][col]);
    }

    protected void checkColumnAndRowValues(int row, int col) throws Exception {
        if(col >= columnNumber || col < 0) {
            throw new Exception("Column number doesn't exist!");
        }
        if(row >= rowNumber || row < 0) {
            throw new Exception("Row number doesn't exist!");
        }
    }
}
