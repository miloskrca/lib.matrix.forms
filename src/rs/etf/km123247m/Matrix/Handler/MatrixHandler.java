package rs.etf.km123247m.Matrix.Handler;

import rs.etf.km123247m.Matrix.IMatrix;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Math.Matrix
 */
// TODO: Implement signatures for all necessary matrix operations.
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
}