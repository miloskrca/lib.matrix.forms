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
        this.matrix = new Object[columnNumber][rowNumber];
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void set(MatrixCell cell) throws Exception {
        checkColumnAndRowValues(cell.getColumn(), cell.getRow());
        matrix[cell.getColumn()][cell.getRow()] = cell.getElement();
    }

    public MatrixCell get(int col, int row) throws Exception {
        checkColumnAndRowValues(col, row);
        return new MatrixCell(row, col, matrix[col][row]);
    }

    protected void checkColumnAndRowValues(int col, int row) throws Exception {
        if(col >= columnNumber || col < 0) {
            throw new Exception("Column number doesn't exist!");
        }
        if(row >= rowNumber || row < 0) {
            throw new Exception("Row number doesn't exist!");
        }
    }
}
