package rs.etf.km123247m.Matrix.Handler;

/**
 * Created by Miloš Krsmanović.
 * Aug 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Handler
 */
// @TODO: This should have never been used like it is now. It can maybe be moved to SymJaMatrixHandler for internal use. Any other mention of this should be replaced with Polynomial and its Terms.
public class CoefficientPowerPair {
    private Object power;
    private Object coefficient;

    public CoefficientPowerPair() {
        this.power = null;
        this.coefficient = null;
    }

    public CoefficientPowerPair(Object coefficient, Object power) {
        this.power = power;
        this.coefficient = coefficient;
    }

    public Object getPower() {
        return power;
    }

    public void setPower(Object power) {
        this.power = power;
    }

    public Object getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Object coefficient) {
        this.coefficient = coefficient;
    }
}
