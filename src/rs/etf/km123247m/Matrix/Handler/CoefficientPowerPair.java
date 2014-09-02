package rs.etf.km123247m.Matrix.Handler;

import org.matheclipse.core.interfaces.IExpr;

/**
 * Created by Miloš Krsmanović.
 * Aug 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Handler
 */
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

    public void setPower(IExpr power) {
        this.power = power;
    }

    public Object getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(IExpr coefficient) {
        this.coefficient = coefficient;
    }
}
