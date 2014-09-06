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
}
