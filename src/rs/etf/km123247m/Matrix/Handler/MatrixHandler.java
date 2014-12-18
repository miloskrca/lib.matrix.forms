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

    /**
     * Adds two rows, stores in second.
     *
     * @param row1 First row.
     * @param row2 Second row.
     * @throws Exception
     */
    public void addRows(int row1, int row2) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        for (int column = 0; column < numOfColumns; column++) {
            Object result = addElements(matrix.get(row1, column).getElement(), matrix.get(row2, column).getElement());
            MatrixCell resultCell = new MatrixCell(row2, column, result);
            matrix.set(resultCell);
        }
    }

    public void addRows(int row1, MatrixCell[] objectRow) throws Exception {
        int numOfColumns = matrix.getColumnNumber();
        for (int column = 0; column < numOfColumns; column++) {
            Object result = addElements(matrix.get(row1, column).getElement(), objectRow[column].getElement());
            MatrixCell resultCell = new MatrixCell(row1, column, result);
            matrix.set(resultCell);
        }
    }

    /**
     * Adds two columns, stores in second.
     *
     * @param column1 First column.
     * @param column2 Second column.
     * @throws Exception
     */
    public void addColumns(int column1, int column2) throws Exception {
        int numOfRows = matrix.getRowNumber();
        for (int row = 0; row < numOfRows; row++) {
            Object result = addElements(matrix.get(row, column1).getElement(), matrix.get(row, column2).getElement());
            MatrixCell resultCell = new MatrixCell(row, column2, result);
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

    public void storeColumn(int column, MatrixCell[] columnCells) throws Exception {
        int numOfRows = matrix.getRowNumber();
        for (int row = 0; column < numOfRows; column++) {
            matrix.set(new MatrixCell(row, column, columnCells[row].getElement()));
        }
    }

    public void subtractWith(IMatrix subtractedMatrix) throws Exception {
        int rowNumber = matrix.getRowNumber();
        int columnNumber = matrix.getColumnNumber();
        for (int row = 0; row < rowNumber; row++) {
            for (int column = 0; column < columnNumber; column++) {
                Object element = subtractElements(
                    matrix.get(row, column).getElement(),
                    subtractedMatrix.get(row, column).getElement()
                );
                matrix.set(new MatrixCell(row, column, element));
            }
        }
    }

    public Object subtractElements(Object element1, Object element2) throws Exception {
        return addElements(element1, calculateNegativeElement(element2));
    }

    public IMatrix add(IMatrix matrix1, IMatrix matrix2, IMatrix resultMatrix) throws Exception {
        if (matrix1.getColumnNumber() != matrix2.getRowNumber()) {
            throw new Exception(
                    "Column and row numbers not appropriate for multiplication: " +
                            matrix1.getColumnNumber() + "!=" + matrix2.getRowNumber()
            );
        }
        for (int i = 0; i < matrix1.getRowNumber(); i++) {
            for (int j = 0; j < matrix2.getColumnNumber(); j++) {
                resultMatrix.set(
                        new MatrixCell(i, j,
                                addElements(
                                        matrix1.get(i, j).getElement(),
                                        matrix2.get(i, j).getElement()
                                )
                        )
                );
            }
        }

        return resultMatrix;
    }

    public IMatrix add(IMatrix matrix1, IMatrix matrix2) throws Exception {
        IMatrix resultMatrix = matrix.createMatrix(matrix1.getRowNumber(), matrix2.getColumnNumber());
        resultMatrix.initWith(getObjectFromString("0"));
        add(matrix1, matrix2, resultMatrix);
        return resultMatrix;
    }

    public IMatrix add(IMatrix matrix) throws Exception {
        IMatrix resultMatrix = matrix.createMatrix(matrix.getRowNumber(), matrix.getColumnNumber());
        resultMatrix.initWith(getObjectFromString("0"));
        add(getMatrix(), matrix, resultMatrix);
        return resultMatrix;
    }

    public IMatrix multiply(IMatrix matrix1, IMatrix matrix2, IMatrix resultMatrix) throws Exception {
        if (matrix1.getColumnNumber() != matrix2.getRowNumber()) {
            throw new Exception(
                    "Column and row numbers not appropriate for multiplication: " +
                            matrix1.getColumnNumber() + "!=" + matrix2.getRowNumber()
            );
        }
        for (int i = 0; i < matrix1.getRowNumber(); i++) {
            for (int j = 0; j < matrix2.getColumnNumber(); j++) {
                for (int k = 0; k < matrix1.getColumnNumber(); k++) {
                    Object tempElement = multiplyElements(
                            matrix1.get(i, k).getElement(),
                            matrix2.get(k, j).getElement()
                    );
                    resultMatrix.set(
                            new MatrixCell(i, j,
                                    addElements(tempElement, resultMatrix.get(i, j).getElement())
                            )
                    );
                }
            }
        }

        return resultMatrix;
    }



    public IMatrix multiply(IMatrix matrix) throws Exception {
        return multiply(getMatrix(), matrix);
    }

    public IMatrix multiply(IMatrix matrix1, IMatrix matrix2) throws Exception {
        IMatrix resultMatrix = matrix.createMatrix(matrix1.getRowNumber(), matrix2.getColumnNumber());
        resultMatrix.initWith(getObjectFromString("0"));
        multiply(matrix1, matrix2, resultMatrix);
        return resultMatrix;
    }

    public Object divideCellElements(Object object1, Object object2) throws Exception {
        return divideElements(object1, object2);
    }

    public IMatrix power(IMatrix matrix, int power) throws Exception {
        if(power == 1) {
            return duplicate(matrix);
        }

        if(power == 0) {
            return diagonal(matrix.getRowNumber(), getObjectFromString("1"));
        }

        IMatrix result = null;
        for (int i = 0; i < power; i++) {
            if(result == null) {
                result = duplicate(matrix);
            } else {
                IMatrix tempResult = matrix.createMatrix(matrix.getRowNumber(), matrix.getColumnNumber());
                tempResult.initWith(getObjectFromString("0"));
                multiply(result, matrix, tempResult);
                result = tempResult;
            }
        }

        return result;
    }

    public IMatrix diagonal(int range, Object element) throws Exception {
        return diagonal(range, range, element);
    }

    public IMatrix diagonal(int rowNumber, int columnNumber, Object element) throws Exception {
        IMatrix diagonal = matrix.createMatrix(rowNumber, columnNumber);
        for (int row = 0; row < rowNumber; row++) {
            for (int column = 0; column < columnNumber; column++) {
                if (row == column) {
                    diagonal.set(new MatrixCell(row, column, element));
                } else {
                    diagonal.set(new MatrixCell(row, column, getObjectFromString("0")));
                }
            }
        }

        return diagonal;
    }

    public IMatrix duplicate(IMatrix matrix) throws Exception {
        int rows = matrix.getRowNumber();
        int columns = matrix.getColumnNumber();
        IMatrix duplicate = matrix.createMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                duplicate.set(
                        new MatrixCell(i, j,
                                getObjectFromString(
                                        matrix.get(i, j).getElement().toString()
                                )
                        )
                );
            }
        }

        return duplicate;
    }

    public IMatrix invertMatrix(IMatrix matrix) throws Exception {
        return multiplyByElement(transpose(coFactor(matrix)), divideElements(getObjectFromString("1"), determinant(matrix)));
    }

    private IMatrix multiplyByElement(IMatrix matrix, Object element) throws Exception {
        int rows = matrix.getRowNumber();
        int columns = matrix.getColumnNumber();
        for (int i=0; i < rows; i++) {
            for (int j=0; j < columns; j++) {
                matrix.set(new MatrixCell(i, j, multiplyElements(matrix.get(i, j).getElement(), element)));
            }
        }

        return matrix;
    }

    public IMatrix transpose(IMatrix matrix) throws Exception {
        IMatrix transposedMatrix = matrix.createMatrix(matrix.getRowNumber(), matrix.getColumnNumber());
        for (int i=0; i < matrix.getRowNumber();i++) {
            for (int j=0;j<matrix.getColumnNumber();j++) {
                transposedMatrix.set(new MatrixCell(j, i, getObjectFromString(matrix.get(i, j).getElement().toString())));
            }
        }
        return transposedMatrix;
    }

    public Object determinant(IMatrix matrix) throws Exception {
        if (matrix.getRowNumber() == 1) {
            return matrix.get(0, 0).getElement();
        }
        if (matrix.getRowNumber() == 2) {
            return subtractElements(
                    multiplyElements(matrix.get(0, 0).getElement(), matrix.get(1, 1).getElement()),
                    multiplyElements(matrix.get(0, 1).getElement(), matrix.get(1, 0).getElement())
            );
        }
        Object sum = getObjectFromString("0");
        for (int i = 0; i < matrix.getColumnNumber(); i++) {
            sum = addElements(
                    sum,
                    multiplyElements(
                            changeSign(i),
                            multiplyElements(
                                    matrix.get(0, i).getElement(),
                                    determinant(createSubMatrix(matrix, 0, i))
                            )
                    )
            );
        }
        return sum;
    }

    public IMatrix createSubMatrix(IMatrix matrix, int excludingRow, int excludingCol) throws Exception {
        IMatrix mat = matrix.createMatrix(matrix.getRowNumber()-1, matrix.getColumnNumber()-1);
        int r = -1;
        for (int i=0;i<matrix.getRowNumber();i++) {
            if (i == excludingRow) {
                continue;
            }
            r++;
            int c = -1;
            for (int j=0; j < matrix.getColumnNumber();j++) {
                if (j == excludingCol) {
                    continue;
                }
                mat.set(new MatrixCell(r, ++c, matrix.get(i, j).getElement()));
            }
        }
        return mat;
    }

    public IMatrix coFactor(IMatrix matrix) throws Exception {
        IMatrix mat = matrix.createMatrix(matrix.getRowNumber(), matrix.getColumnNumber());
        for (int i=0;i<matrix.getRowNumber();i++) {
            for (int j=0; j<matrix.getColumnNumber();j++) {
                mat.set(new MatrixCell(i, j,
                        multiplyElements(
                                changeSign(i),
                                multiplyElements(
                                        changeSign(j),
                                        determinant(createSubMatrix(matrix, i, j))
                                )
                        )
                ));
            }
        }

        return mat;
    }

    private Object changeSign(int i) throws Exception {
        if (i % 2 == 0) {
            return getObjectFromString("1");
        }
        return getObjectFromString("-1");
    }

    public boolean isSingular() throws Exception {
        IMatrix matrix = getMatrix();
        Object total = getObjectFromString("1");
        for (int i=0;i< this.matrix.getRowNumber();i++) {
            for (int j=0; j< this.matrix.getColumnNumber();j++) {
                if(i == j) {
                    total = multiplyElements(total, matrix.get(i, j).getElement());
                }
            }
        }

        return compare(total, getObjectFromString("0")) == 0;
    }

    public boolean containsSymbol() throws Exception {
        IMatrix matrix = getMatrix();
        for (int i=0;i< this.matrix.getRowNumber();i++) {
            for (int j=0; j< this.matrix.getColumnNumber();j++) {
                if(isElementSymbol(matrix.get(i, j).getElement())) {
                    return true;
                }
            }
        }

        return false;
    }

    protected abstract boolean isElementSymbol(Object element);

    protected abstract Object addElements(Object element1, Object element2) throws Exception;

    protected abstract Object multiplyElements(Object element1, Object element2) throws Exception;

    protected abstract Object divideElements(Object element1, Object element2) throws Exception;

    public abstract Object calculateNegativeElement(Object element) throws Exception;

    public abstract int comparePowersOfElements(Object element1, Object element2) throws Exception;

    public abstract boolean isZeroElement(Object element) throws Exception;

    public abstract int compare(Object element1, Object element2) throws Exception;

    public abstract Object getObjectFromString(String string) throws Exception;

    public abstract boolean isElementDividing(Object element1, Object element2) throws Exception;

    public abstract int getHighestPower(Object element) throws Exception;

}