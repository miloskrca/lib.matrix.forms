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

    public void set(int col, int row, Object t) throws Exception;
    public Object get(int col, int row) throws Exception;

}
