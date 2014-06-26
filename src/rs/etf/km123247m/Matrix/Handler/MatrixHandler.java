package rs.etf.km123247m.Matrix.Handler;

import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Math.Matrix
 */
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

    public MatrixCell[] multipleRowWithElement(int row, Object element) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        MatrixCell[] resultRow = new MatrixCell[numOfColumns];
        for (int column = 0; column < numOfColumns; column++) {
            Object result = multiplyElements(matrix.get(row, column).getElement(), element);
            MatrixCell resultCell = new MatrixCell(row, column, result);
            resultRow[column] = resultCell;
        }
        return resultRow;
    }

    public MatrixCell[] multipleColumnWithElement(int column, Object element) throws Exception {
        int numOfRows = matrix.getRowNumber();
        MatrixCell[] resultColumn = new MatrixCell[numOfRows];
        for (int row = 0; row < numOfRows; row++) {
            Object result = multiplyElements(matrix.get(row, column).getElement(), element);
            MatrixCell resultCell = new MatrixCell(row, column, result);
            resultColumn[row] = resultCell;
        }
        return resultColumn;
    }

    public MatrixCell[] divideRowWithElement(int row, Object element) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        MatrixCell[] resultRow = new MatrixCell[numOfColumns];
        for (int column = 0; column < numOfColumns; column++) {
            Object result = divideElements(matrix.get(row, column).getElement(), element);
            MatrixCell resultCell = new MatrixCell(row, column, result);
            resultRow[column] = resultCell;
        }
        return resultRow;
    }

    public void addRows(int row1, MatrixCell[] objectRow) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        for (int column = 0; column < numOfColumns; column++) {
            Object result = addElements(matrix.get(row1, column).getElement(), objectRow[column].getElement());
            MatrixCell resultCell = new MatrixCell(row1, column, result);
            matrix.set(resultCell);
        }
    }

    public void addRows(int row1, int row2) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        for (int column = 0; column < numOfColumns; column++) {
            Object result = addElements(matrix.get(row1, column).getElement(), matrix.get(row2, column).getElement());
            MatrixCell resultCell = new MatrixCell(row1, column, result);
            matrix.set(resultCell);
        }
    }

    public void addColumns(int column1, MatrixCell[] objectColumn) throws Exception {
        int numOfRows = matrix.getRowNumber();
        for (int row = 0; row < numOfRows; row++) {
            Object result = addElements(matrix.get(row, column1).getElement(), objectColumn[row].getElement());
            MatrixCell resultCell = new MatrixCell(row, column1, result);
            matrix.set(resultCell);
        }
    }

    public void storeRow(int row, MatrixCell[] rowCells) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        for (int column = 0; column < numOfColumns; column++) {
            matrix.set(new MatrixCell(row, column, rowCells[column].getElement()));
        }
    }

    public Object divideCellElements(Object object1, Object object2) throws Exception {
        return divideElements(object1, object2);
    }

    public abstract Object divideCellElementsAndReturnRemainder(Object element1, Object element2) throws Exception;

    protected abstract Object addElements(Object element1, Object element2) throws Exception;

    protected abstract Object multiplyElements(Object element1, Object element2) throws Exception;

    protected abstract Object divideElements(Object element1, Object element2) throws Exception;

    public abstract Object calculateNegativeElement(Object element) throws Exception;

    public abstract int comparePowersOfElements(Object element1, Object element2);

    public abstract boolean isZeroElement(Object element);
}