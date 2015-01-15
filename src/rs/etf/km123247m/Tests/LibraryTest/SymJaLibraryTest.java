package rs.etf.km123247m.Tests.LibraryTest;


import org.ejml.data.Complex64F;
import org.junit.Test;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Tests.PolynomialRootFinder;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class SymJaLibraryTest {

    private EvalUtilities util = new EvalUtilities();

    @Test
    public void doSomeTests() throws Exception {
        String element1 = "-x^2+4*x-4";
        String element2 = "x-3";
        IExpr results = util.evaluate("PolynomialQuotientRemainder[" + element1 + "," + element2 + ", x]");
        assert "{-x+1,-1}".equals(results.toString());

        String element3 = "x^2-4*x+4";
        IExpr result = util.evaluate("Factor[" + element3 + "]");
        assert "(x-2)^2".equals(result.toString());

        String element4 = "x^4-19*x^3+77*x^2+251*x-1430";
        result = util.evaluate(element4);
//        System.out.println(result);

//        Complex64F[] b = PolynomialRootFinder.findRoots(-1430,251,77,-19,1);
//        for (Complex64F aB : b) {
//            System.out.println(aB.getReal() + " " + aB.getImaginary() + "i");
//            System.out.println(aB.getImaginary() % 1 == 0 ? "is zero" : "not zero");
//        }
    }

}
