package rs.etf.km123247m.Polynomial;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Polynomial
 */
public class Term implements Comparable {

    public static short MINUS = 0;
    public static short PLUS = 1;

    private int power;
    private int coefficient;
    private String variable;
    private short sign;

    public Term(short sign, int coefficient, String variable, int power) throws Exception {
        this.setSign(sign);
        this.setVariable(variable);
        this.setPower(power);
        this.setCoefficient(coefficient);
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public short getSign() {
        return sign;
    }

    public void setSign(short sign) throws Exception {
        if (sign == PLUS && sign == MINUS) {
            this.sign = sign;
        } else {
            throw new Exception("Invalid sign!");
        }
    }

    @Override
    public int compareTo(Object o) {
        Term t = (Term) o;
        if (t.getCoefficient() == coefficient
                && t.getPower() == power
                && t.getSign() == sign
                && t.getVariable().equals(variable)) {
            return 0;
        } else if (t.getPower() > power && t.getVariable().equals(variable)) {
            // maybe do a fine grain compare
            return -1;
        }
        return 1;
    }
}
