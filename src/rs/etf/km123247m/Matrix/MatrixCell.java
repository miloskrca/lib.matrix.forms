package rs.etf.km123247m.Matrix;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix
 */
public class MatrixCell {
    private int row;
    private int column;
    private Object element;

    public MatrixCell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public MatrixCell(int row, int column, Object element) {
        this.row = row;
        this.column = column;
        this.element = element;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }
}
