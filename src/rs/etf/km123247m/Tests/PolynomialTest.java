package rs.etf.km123247m.Tests;

import org.jscience.mathematics.function.Polynomial;
import org.jscience.mathematics.function.Term;
import org.jscience.mathematics.function.Variable;
import org.jscience.mathematics.number.Rational;
import org.junit.Test;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class PolynomialTest {

    @Test
    public void createSomePolynomials() {

        Variable<Rational> varX = new Variable.Local<Rational>("x"); // x
        Term term = Term.valueOf(varX, 5); // x^5
        Term term1 = Term.valueOf(varX, 2); // x^2
        Term term2 = Term.valueOf(varX, 3); // x^3
        Term term3 = Term.valueOf(varX, 4); // x^4
        Polynomial<Rational> poly = Polynomial.valueOf(Rational.valueOf(2, 1), varX); // 2/1 * x
        Polynomial<Rational> poly2 = Polynomial.valueOf(Rational.ONE, term); // 1 * x^5
        Polynomial<Rational> poly3 = Polynomial.valueOf(Rational.ONE, term1)
                .minus(Polynomial.valueOf(Rational.valueOf(1, 5), term2))
                .plus(Polynomial.valueOf(Rational.valueOf(-2, 5), term3));

        System.out.println(poly);
        System.out.println(poly2);
        System.out.println(poly.plus(poly2));
        System.out.println(poly3.toText());
        System.out.println(poly3.getTerms());
        System.out.println(term);
    }
}
