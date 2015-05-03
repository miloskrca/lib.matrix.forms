package rs.etf.km123247m.Matrix.Implementation;

import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
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

    @Override
    public void initWith(Object element) throws Exception {
        for (int j = 0; j < rowNumber; j++) {
            for (int k = 0; k < columnNumber; k++) {
                set(new MatrixCell(j, k, element));
            }
        }
    }

    @Override
    public IMatrix createMatrix(int rowNumber, int columnNumber) throws Exception {
        return new ArrayMatrix(rowNumber, columnNumber);
    }

    protected void checkColumnAndRowValues(int row, int col) throws Exception {
        if (col >= columnNumber || col < 0) {
            throw new Exception("Column number " + col + " doesn't exist!");
        }
        if (row >= rowNumber || row < 0) {
            throw new Exception("Row number " + row + " doesn't exist!");
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
