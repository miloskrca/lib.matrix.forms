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
    public static char MINUS_CHAR = '-';
    public static char PLUS_CHAR = '+';
    public static char NULL = '\u0000';

    private int power;
    private String coefficient;
    private char variable;
    private short sign;

    public Term(short sign, String coefficient, char variable, int power) {
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

    public String getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public char getVariable() {
        return variable;
    }

    public void setVariable(char variable) {
        this.variable = variable;
    }

    public short getSign() {
        return sign;
    }

    public void setSign(short sign) {
        this.sign = sign;
    }

    @Override
    public int compareTo(Object o) {
        Term t = (Term) o;
        if (t.getCoefficient().equals(coefficient)
                && t.getPower() == power
                && t.getSign() == sign
                && t.getVariable() == variable) {
            return 0;
        } else if (t.getPower() > power && t.getVariable() == variable) {
            // maybe do a fine grain compare
            return -1;
        }
        return 1;
    }

    public static Term getZeroTerm() {
        return new Term(PLUS, "0", NULL, 1);
    }

    public String toString() {
        String signString = sign == MINUS ? "-" : "";
        String variableString = power == 0 ? "" : variable == NULL ? "" : String.valueOf(variable);
        String coefficientString;
        if(variable == NULL) {
            coefficientString = coefficient;
        } else {
            coefficientString = coefficient.equals("1") ? "" : coefficient + "*";
        }
        String powerString = power == 1 ? "" : power == 0 ? "" : "^" + power;

        String returnString = signString + coefficientString + variableString + powerString;

        // if the term is only a coefficient the "*" sign is not necessary
        if(returnString.length() > 1 && returnString.charAt(returnString.length() - 1) == '*') {
            returnString = returnString.substring(0, returnString.length() - 1);
        }

        return returnString.length() == 0 ? "1" : returnString;
    }

    public char getSignChar() {
        return sign == PLUS ? PLUS_CHAR : MINUS_CHAR;
    }
}
