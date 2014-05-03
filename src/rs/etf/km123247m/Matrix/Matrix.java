package rs.etf.km123247m.Matrix;

import java.util.ArrayList;

/**
 * Created by Miloš Krsmanović.
 * 2014
 *
 * package: rs.etf.km123247m.Matrix
 */
public class Matrix {
    private final int rowNumber;
    private final int columnNumber;
    private ArrayList<ArrayList<Object>> matrix;

    public Matrix(int columnNumber, int rowNumber) {
        this.columnNumber = columnNumber;
        this.rowNumber = rowNumber;
        this.matrix = new ArrayList<ArrayList<Object>>();
    }
    public Matrix(ArrayList<ArrayList<Object>> matrix) {
        this.columnNumber = matrix.size();
        this.rowNumber = matrix.size() > 0 ? matrix.get(0).size(): 0;
        this.matrix = matrix;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public ArrayList<ArrayList<Object>> getMatrix() {
        return matrix;
    }

    public void set(int col, int row, Object t) throws Exception {
        if(col >= columnNumber) {
            throw new Exception("Column number doesn't exist!");
        }
        if(row >= rowNumber) {
            throw new Exception("Row number doesn't exist!");
        }
        // @TODO: fix IndexOutOfBounds exception
        this.getMatrix().get(col).set(row, t);
    }

    public Object get(int col, int row) throws Exception {
        if(col >= columnNumber) {
            throw new Exception("Column number doesn't exist!");
        }
        if(row >= rowNumber) {
            throw new Exception("Row number doesn't exist!");
        }
        return this.getMatrix().get(col).get(row);
    }
}
