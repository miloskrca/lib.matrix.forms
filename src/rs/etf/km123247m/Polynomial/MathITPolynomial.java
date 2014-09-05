package rs.etf.km123247m.Polynomial;

import org.mathIT.algebra.ExponentComparator;
import org.mathIT.algebra.Polynomial;
import rs.etf.km123247m.PropertyManager.PropertyManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Miloš Krsmanović.
 * Sep 2014
 * <p/>
 * package: rs.etf.km123247m.Polynomial
 */
public class MathITPolynomial {
    private final Polynomial object;
    private static final double ACCURACY = Double.parseDouble(PropertyManager.getProperty("accuracy"));

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

    /**
     * Multiplies element with supplied element.
     *
     * Not using multiply from org.mathIT.algebra.Polynomial because it
     * throws an ConcurrentModificationException in TreeMap
     *
     * @param element Element to multiply with
     * @return Result of the multiplication
     * @throws ParseException
     */
    public MathITPolynomial multiply(MathITPolynomial element) throws ParseException {
        if(isZero() || element.isZero()) {
            MathITPolynomial p = new MathITPolynomial();
            p.put(0, (double) 0);
            return p.roundDown();
        }
        MathITPolynomial e = new MathITPolynomial(oMultiply(element.getObject()));
//        MathITPolynomial e = new MathITPolynomial(object.multiply(element.getObject()));

        return e.roundDown();
    }

    /**
     * Multiplies this polynomial with the given polynomial <i>q</i>.
     * @param q the polynomial to be multiplied with this polynomial
     * @return the product of this polynomial times <i>q</i>
     */
    protected Polynomial oMultiply( Polynomial q ) {
        Polynomial p = object,
                r = new Polynomial(new ExponentComparator());

        int i, j;
        Integer k;
        double tmp;
        int degP = p.deg();
        int degQ = q.deg();

        for (i = 0; i <= degP; i++) {
            for (j = 0; j <= degQ; j++) {
                if ( p.get(i) != null && q.get(j) != null ) {
                    k = i + j;
                    if ( r.get(k) != null ) {
                        tmp = r.get(k);
                    } else {
                        tmp = 0;
                    }
                    tmp += p.get(i) * q.get(j);
                    r.put( k, tmp);
                }
            }
        }

        // THIS THROWS ConcurrentModificationException  in TreeMap

        // clear all terms whose coefficients are near to zero:
//        Set<Integer> keys = r.keySet();
//        for( Integer e : keys ) {
//            if ( Math.abs(r.get(e)) < ACCURACY ) r.remove(e);
//        }
        return r;
    }

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
        ArrayList<Map.Entry<Integer, Double>> toDeleteList = new ArrayList<Map.Entry<Integer, Double>>();
        for (Map.Entry<Integer, Double> term : object.entrySet()) {
            Double coefficient = term.getValue();
            // if the coefficient is too small
            if (Math.abs(coefficient) < ACCURACY) {
                    toDeleteList.add(term);
            }
        }

        // Do removing separate because otherwise ConcurrentModificationException
        // is thrown in TreeMap
        for(Map.Entry<Integer, Double> term : toDeleteList) {
            object.remove(term.getKey());
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
