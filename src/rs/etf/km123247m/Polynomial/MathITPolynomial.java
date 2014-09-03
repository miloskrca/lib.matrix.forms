package rs.etf.km123247m.Polynomial;

import org.mathIT.algebra.Polynomial;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by Miloš Krsmanović.
 * Sep 2014
 * <p/>
 * package: rs.etf.km123247m.Polynomial
 */
public class MathITPolynomial {
    private final Polynomial object;

    public MathITPolynomial(org.mathIT.algebra.Polynomial polynomial) {
        object = polynomial;
    }

    public MathITPolynomial() {
        object = new Polynomial();
    }

    public MathITPolynomial[] divide(MathITPolynomial element) throws ParseException {
        Polynomial[] array = object.divide(element.getObject());
        return new MathITPolynomial[] {
            (new MathITPolynomial(array[0])).roundDown(),
            (new MathITPolynomial(array[1])).roundDown()
        };
    }

    public MathITPolynomial multiply(MathITPolynomial element) throws ParseException {
        if(isZero() || element.isZero()) {
            MathITPolynomial p = new MathITPolynomial();
            p.put(0, (double) 0);
            return p.roundDown();
        }
        MathITPolynomial e = new MathITPolynomial(object.multiply(element.getObject()));

        return e.roundDown();
    }

    // @TODO: check if this is good enough (only 0.0, maybe rounding needed first)
    public boolean isZero() {
        for (Map.Entry<Integer, Double> integerDoubleEntry : object.entrySet()) {
            if (integerDoubleEntry.getValue() != 0.0) {
                return false;
            }
        }
        return true;
    }

    public MathITPolynomial add(MathITPolynomial element) throws ParseException {
        MathITPolynomial newPolynomial = new MathITPolynomial();
        for (Map.Entry<Integer, Double> term : object.entrySet()) {
            newPolynomial.put(term.getKey(), term.getValue());
        }

        for (Map.Entry<Integer, Double> term : element.getObject().entrySet()) {
            if (newPolynomial.containsKey(term.getKey())) {
                int power = term.getKey();
                double coefficient = object.get(power);
                newPolynomial.remove(power);
                newPolynomial.put(power, term.getValue() + coefficient);
            } else {
                newPolynomial.put(term.getKey(), term.getValue());
            }
        }
        return newPolynomial.roundDown();
    }

    public MathITPolynomial subtract(MathITPolynomial element) throws ParseException {
        MathITPolynomial newPolynomial = new MathITPolynomial();
        for (Map.Entry<Integer, Double> term : object.entrySet()) {
            newPolynomial.put(term.getKey(), term.getValue());
        }

        for (Map.Entry<Integer, Double> term : element.getObject().entrySet()) {
            if (newPolynomial.containsKey(term.getKey())) {
                int power = term.getKey();
                double coefficient = newPolynomial.getObject().get(power);
                newPolynomial.remove(power);
                newPolynomial.put(power, coefficient - term.getValue());
            } else {
                newPolynomial.put(term.getKey(), - term.getValue());
            }
        }
        return newPolynomial.roundDown();
    }

    public MathITPolynomial roundDown() throws ParseException {
        DecimalFormat df = new DecimalFormat("0.00000000000000");

        for (Map.Entry<Integer, Double> term : object.entrySet()) {
            Double coefficient = term.getValue();
            // if the coefficient is too small or too large
            if(coefficient.toString().contains("E")) {
                String format = df.format(coefficient);
                double finalValue = (Double)df.parse(format) ;
                term.setValue(finalValue);
            }
        }

        return this;
    }

    private Double remove(int key) {
        return object.remove(key);
    }

    private boolean containsKey(Integer key) {
        return object.containsKey(key);
    }

    public Double put(int power, double coefficient) {
        return object.put(power, coefficient);
    }

    public int deg() {
        return object.deg();
    }

    public java.util.Set<Map.Entry<Integer, Double>> entrySet() {
        return object.entrySet();
    }

    public Map.Entry<Integer, Double> firstEntry() {
        return object.firstEntry();
    }

    public int size() {
        return object.size();
    }

    public String toString() {
        return object.toString();
    }

    public Polynomial getObject() {
        return object;
    }
}
