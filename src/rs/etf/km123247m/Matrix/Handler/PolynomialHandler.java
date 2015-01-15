package rs.etf.km123247m.Matrix.Handler;

/**
 * Created by Miloš Krsmanović.
 * Jan 2015
 * <p/>
 * package: rs.etf.km123247m.Matrix.Handler
 */
public interface PolynomialHandler {
    public Object[] findRoots(double[] coefficients);
    public String mergeRoots(Object[] roots);
}
