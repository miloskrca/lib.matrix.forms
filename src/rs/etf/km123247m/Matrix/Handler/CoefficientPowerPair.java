package rs.etf.km123247m.Matrix.Handler;

import org.matheclipse.core.interfaces.IExpr;

/**
 * Created by Miloš Krsmanović.
 * Aug 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Handler
 */
public class CoefficientPowerPair {
    private IExpr power;
    private IExpr coefficient;

    public CoefficientPowerPair() {
        this.power = null;
        this.coefficient = null;
    }

    public CoefficientPowerPair(IExpr coefficient, IExpr power) {
        this.power = power;
        this.coefficient = coefficient;
    }

    public IExpr getPower() {
        return power;
    }

    public void setPower(IExpr power) {
        this.power = power;
    }

    public IExpr getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(IExpr coefficient) {
        this.coefficient = coefficient;
    }
}
