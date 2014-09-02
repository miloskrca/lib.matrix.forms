package rs.etf.km123247m.Polynomial;

import org.mathIT.algebra.Polynomial;

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

    public MathITPolynomial[] divide(MathITPolynomial element) {
        Polynomial[] array = object.divide(element.getObject());
        return new MathITPolynomial[] {
            new MathITPolynomial(array[0]),
            new MathITPolynomial(array[1])
        };
    }

    public MathITPolynomial multiply(MathITPolynomial element) {
        return new MathITPolynomial(object.multiply(element.getObject()));
    }

    public MathITPolynomial add(MathITPolynomial element) {
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
        return newPolynomial;
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

    public Map.Entry<Integer, Double> lastEntry() {
        return object.lastEntry();
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
