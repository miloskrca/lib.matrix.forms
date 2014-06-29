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

    public ArrayMatrix(int rowNumber, int columnNumber) {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < rowNumber; row++) {
            sb.append("|");
            for (int col = 0; col < columnNumber; col++) {
                sb.append(" ");
                sb.append(matrix[row][col].toString());
                sb.append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }
}
