package rs.etf.km123247m.Matrix;

/**
 * Created by Miloš Krsmanović.
 * 2014
 *
 * package: rs.etf.km123247m.Matrix
 */
public interface IMatrix {

    public int getColumnNumber();
    public int getRowNumber();

    public void set(MatrixCell cell) throws Exception;
    public MatrixCell get(int row, int col) throws Exception;

    public void initWith(Object element) throws Exception;

//    public void switchRows(int row1, int row2) throws Exception;
//    public void switchColumns(int column1, int column2) throws Exception;
//    public MatrixCell[] multipleRowWithElement(int row, Object element) throws Exception;
//    public MatrixCell[] multipleColumnWithElement(int column, Object element) throws Exception;
//    public void addRows(int row1, int row2) throws Exception;
//    public void addRows(int row1, MatrixCell[] objectRow) throws Exception;
//    public void addColumns(int column1, int column2) throws Exception;
//    public void addColumns(int column1, MatrixCell[] objectColumn) throws Exception;
//    public void storeRow(int row, MatrixCell[] rowCells) throws Exception;
//    public void storeColumn(int column, MatrixCell[] columnCells) throws Exception;
//    public void subtractWith(IMatrix subtractedMatrix) throws Exception;
//    public Object subtractElements(Object element1, Object element2) throws Exception;
//    // TODO: probably shouldn't exist
//    public IMatrix add(IMatrix matrix1, IMatrix matrix2, IMatrix resultMatrix) throws Exception;
//    public IMatrix add(IMatrix matrix1, IMatrix matrix2) throws Exception;
//    public IMatrix add(IMatrix matrix) throws Exception;
//    // TODO: probably shouldn't exist
//    public IMatrix multiply(IMatrix matrix1, IMatrix matrix2, IMatrix resultMatrix) throws Exception;
//    public IMatrix multiply(IMatrix matrix1, IMatrix matrix2) throws Exception;
//    public IMatrix multiply(IMatrix matrix) throws Exception;
//    public Object divideCellElements(Object object1, Object object2) throws Exception;
//    public IMatrix power(IMatrix matrix, int power) throws Exception;
//    public IMatrix diagonal(int range, Object element) throws Exception;
//    public IMatrix diagonal(int rowNumber, int columnNumber, Object element) throws Exception;
//    public IMatrix duplicate(IMatrix matrix) throws Exception;
//    public IMatrix invertMatrix(IMatrix matrix) throws Exception;
////    private IMatrix multiplyByElement(IMatrix matrix, Object element) throws Exception;
//    public IMatrix transpose(IMatrix matrix) throws Exception;
//    public Object determinant(IMatrix matrix) throws Exception;
//    public IMatrix createSubMatrix(IMatrix matrix, int excludingRow, int excludingCol) throws Exception;
//    public IMatrix coFactor(IMatrix matrix) throws Exception;
////    private Object changeSign(int i) throws Exception;
//    public boolean isSingular() throws Exception;
//    public boolean containsSymbol() throws Exception;
}
