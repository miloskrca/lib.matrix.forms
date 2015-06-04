package rs.etf.km123247m.Tests.LibraryTest;


import org.junit.Test;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.form.tex.TeXFormFactory;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Matrix.Handler.Implementation.EJMLPolynomialHandler;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class SymJaLibraryTest {

    private EvalUtilities util = new EvalUtilities();
    private SymJaMatrixHandler handler = new SymJaMatrixHandler(new ArrayMatrix(1, 1));

    @Test
    public void testFactorisation() throws Exception {
        String element1 = "-x^2+4*x-4";
        String element2 = "x-3";
        IExpr results = util.evaluate("PolynomialQuotientRemainder[" + element1 + "," + element2 + ", x]");
        assertEquals("{-x+1,-1}", results.toString());

        String element3 = "x^2-4*x+4";
        IExpr result = util.evaluate("Factor[" + element3 + "]");
        assertEquals("(-x+2)^2", result.toString());

        String[] polynomialsForFactorisation = new String[]{
                "x^3-5/3*x^2+5/3*x-2/3",
                "x^2-5*x-2",
                "x^2+2*x+10",
                "x^2-4*x+7",
                "x^3-7*x^2+12*x",
                "x^2-4*x+4",
                "x^3+10*x^2+169*x",
                "x^4-19*x^3+77*x^2+251*x-1430",
                "x^4+2*x^3-11*x^2-12*x+36"
        };

        for (String poly : polynomialsForFactorisation) {
            ArrayList<Object> roots = handler.getRoots(handler.getObjectFromString(poly));
            ArrayList<Object> factors = handler.factor(handler.getObjectFromString(poly));
            assertEquals(
                    "Expected successful factorisation for " + poly + " !",
                    handler.getIExprFactorisation().isFactorisationCorrect(),
                    true
            );
            if (poly.equals("x^3-5/3*x^2+5/3*x-2/3")) {
                assertEquals("Roots not OK!", "[2/3, -I*1/2*3^(1/2)+1/2, I*1/2*3^(1/2)+1/2]", roots.toString());
                assertEquals("Factors not OK!", "[x-2/3, x+I*1/2*3^(1/2)-1/2, x-I*1/2*3^(1/2)-1/2]", factors.toString());
            } else if (poly.equals("x^2-5*x-2")) {
                assertEquals("Roots not OK!", "[-1/2*33^(1/2)+5/2, 1/2*33^(1/2)+5/2]", roots.toString());
                assertEquals("Factors not OK!", "[x+1/2*33^(1/2)-5/2, x-1/2*33^(1/2)-5/2]", factors.toString());
            } else if (poly.equals("x^2+2*x+10")) {
                assertEquals("Roots not OK!", "[-1-I*3, -1+I*3]", roots.toString());
                assertEquals("Factors not OK!", "[x+1+I*3, x+1-I*3]", factors.toString());
            } else if (poly.equals("x^2-4*x+7")) {
                assertEquals("Roots not OK!", "[-I*3^(1/2)+2, I*3^(1/2)+2]", roots.toString());
                assertEquals("Factors not OK!", "[x+I*3^(1/2)-2, x-I*3^(1/2)-2]", factors.toString());
            } else if (poly.equals("x^3-7*x^2+12*x")) {
                assertEquals("Roots not OK!", "[0, 3, 4]", roots.toString());
                assertEquals("Factors not OK!", "[x, x-4, x-3]", factors.toString());
            } else if (poly.equals("x^2-4*x+4")) {
                assertEquals("Roots not OK!", "[2, 2]", roots.toString());
                assertEquals("Factors not OK!", "[(-x+2)^2]", factors.toString());
            } else if (poly.equals("x^3+10*x^2+169*x")) {
                assertEquals("Roots not OK!", "[0, -5-I*12, -5+I*12]", roots.toString());
                assertEquals("Factors not OK!", "[x, x+5+I*12, x+5-I*12]", factors.toString());
            } else if (poly.equals("x^4-19*x^3+77*x^2+251*x-1430")) {
                assertEquals("Roots not OK!", "[5, 11, -1/2*113^(1/2)+3/2, 1/2*113^(1/2)+3/2]", roots.toString());
                assertEquals("Factors not OK!", "[x-5, x-11, x+1/2*113^(1/2)-3/2, x-1/2*113^(1/2)-3/2]", factors.toString());
            } else if (poly.equals("x^4+2*x^3-11*x^2-12*x+36")) {
                assertEquals("Roots not OK!", "[-3, -3, 2, 2]", roots.toString());
                assertEquals("Factors not OK!", "[(-x+2)^2, (x+3)^2]", factors.toString());
            } else {
                System.out.println("----------------------------------------");
                System.out.println("Poly: " + poly);
                System.out.println("Final root list: " + roots);
                System.out.println("Final factor list: " + factors);
                System.out.println("----------------------------------------");

            }
        }
    }

    @Test
    public void testHighestPower() throws Exception {
        HashMap<String, Integer> polynomials = new HashMap<String, Integer>();
        polynomials.put("x^3-5/3*x^2+5/3*x-2/3", 3);
        polynomials.put("x^2-5*x-2", 2);
        polynomials.put("3*x+3", 1);
        polynomials.put("8", 0);
        polynomials.put("x", 1);

        for (String polynomial : polynomials.keySet()) {
            IExpr poly = util.evaluate(polynomial);
            assertEquals(
                    "Power not equal for " + polynomial + "!",
                    (int) polynomials.get(polynomial),
                    handler.getHighestPower(poly)
            );
        }

    }

    @Test
    public void testTex() throws Exception {
        TeXFormFactory f = new TeXFormFactory();
        StringBuffer sb = new StringBuffer();
        f.convert(sb, handler.getObjectFromString("-I*1/2*3^(1/2)+1/2"), 0);
        assertEquals(
                "Expected LaTex",
                "\\frac{1}{2}+\\left( \\frac{-1}{2}\\,i \\right) \\,\\sqrt{3}",
                sb.toString()
        );
    }

    @Test
    public void testEJML() throws Exception {
        String[] polynomialsForFactorisation = new String[]{
                "x^3-5*x^2-7*x+4"
        };
        for (String poly : polynomialsForFactorisation) {
            Object currentElement = handler.getObjectFromString(poly);
            int highestPower = handler.getHighestPower(currentElement);
            double[] coefficients = new double[highestPower + 1];
            for (int i = 0; i <= highestPower; i++) {
                coefficients[i] = Double.parseDouble(handler.getCoefficientForPower(currentElement, i).toString());
            }

            EJMLPolynomialHandler polyHandler = new EJMLPolynomialHandler();
            Object[] tempRoots = polyHandler.findRoots(coefficients);
            String roots = new ArrayList<Object>(Arrays.asList(tempRoots)).toString();
            String factors = polyHandler.rootsToFactors(tempRoots);
            handler.factor(handler.getObjectFromString(poly));
            assertEquals(
                    "Expected unsuccessful factorisation for " + poly + " !",
                    handler.getIExprFactorisation().isFactorisationCorrect(),
                    false
            );
            if (poly.equals("x^3-5*x^2-7*x+4")) {
                assertEquals("Roots not OK!", "[6.048045849826134, -1.491478698251567, 0.4434328484254325]", roots);
                assertEquals("Factors not OK!", "(x-6.048045849826134)*(x+1.491478698251567)*(x-0.4434328484254325)", factors);
            } else {
                System.out.println("----------------------------------------");
                System.out.println("Poly: " + poly);
                System.out.println(new ArrayList<Object>((Arrays.asList(tempRoots))) + "");
                System.out.println(polyHandler.rootsToFactors(tempRoots));
                System.out.println("----------------------------------------");
            }

        }
    }

    // Factor
    /*
    if (!poly.equals(util.evaluate("Factor[" + poly + "]").toString()) && !util.evaluate("Factor[" + poly + "]").toString().contains(Term.X + "^")) {
        System.out.print("GOOD ");
    } else {
        System.out.print("BAD ");
    }
    System.out.println("Factoring: " + util.evaluate("Factor[" + poly + "]").toString());
    int rootNum = handler.getHighestPower(handler.getObjectFromString(poly));
    System.out.println("Expected root number: " + rootNum);
    result = util.evaluate("Solve[" + poly + "==0,x]");
    ArrayList<Object> roots = new ArrayList<Object>();
    for (IExpr leaf : result.leaves()) {
        if (leaf.isList()) {
            for (IExpr leaf2 : leaf.leaves()) {
                if (leaf2.isRuleAST()) {
                    String root = leaf2.getAt(2).toString();
                    IExpr o = util.evaluate(root);
                    System.out.println("x: " + root);
                    roots.add(o);
                }
            }
        }
    }
    ArrayList<Object> factors = new ArrayList<Object>();
    System.out.print("Factors: ");
    for (Object r : roots) {
        String sFactor;
        if (handler.isZeroElement(r)) {
            sFactor = String.valueOf(Term.X);
        } else {
            sFactor = "x-(" + r.toString() + ")";
        }
        factors.add(handler.getObjectFromString(sFactor));
        System.out.print(sFactor + " | ");
    }
    System.out.println();
    String tempPolyBase = "";
    for (Object f : factors) {
        tempPolyBase += "*(" + f.toString() + ")";
    }
    tempPolyBase = tempPolyBase.substring(1);

    if (rootNum > factors.size()) {

        System.out.println("Not enough factors!");
        for (Set<Object> setOfFactorCombinations : combinations(factors, rootNum - factors.size())) {
            String setOfFactorCombinationsString = tempPolyBase;
            for(Object factorCombinationObject: setOfFactorCombinations) {
                setOfFactorCombinationsString += "*(" + factorCombinationObject + ")";
            }
            Object tempPolyO = handler.getObjectFromString(setOfFactorCombinationsString);
            tempPolyO = util.evaluate("Expand[" + tempPolyO.toString() + "]");
            if (handler.compare(tempPolyO, handler.getObjectFromString(poly)) == 0) {
                System.out.println("Missing factors: " + setOfFactorCombinations);
                tempPolyBase = setOfFactorCombinationsString;
            }
        }
    }
    System.out.print("Factors are ");
    Object tempPolyO = handler.getObjectFromString(tempPolyBase);
    tempPolyO = util.evaluate("Expand[" + tempPolyO.toString() + "]");
    if (handler.compare(tempPolyO, handler.getObjectFromString(poly)) == 0) {
        System.out.println("GOOD");
    } else {
        System.out.println("BAD");
        System.out.println(tempPolyO);
    }
    System.out.println("------------------------------");
    */


//    public Set<Set<Object>> combinations(List<Object> groupSize, int k) {
//
//        Set<Set<Object>> allCombos = new HashSet<Set<Object>>();
//        // base cases for recursion
//        if (k == 0) {
//            // There is only one combination of size 0, the empty team.
//            allCombos.add(new HashSet<Object>());
//            return allCombos;
//        }
//        if (k > groupSize.size()) {
//            // There can be no teams with size larger than the group size,
//            // so return allCombos without putting any teams in it.
//            return allCombos;
//        }
//
//        // Create a copy of the group with one item removed.
//        List<Object> groupWithoutX = new ArrayList<Object>(groupSize);
//        Object x = groupWithoutX.remove(groupWithoutX.size() - 1);
//
//        Set<Set<Object>> combosWithoutX = combinations(groupWithoutX, k);
//        Set<Set<Object>> combosWithX = combinations(groupWithoutX, k - 1);
//        for (Set<Object> combo : combosWithX) {
//            combo.add(x);
//        }
//        allCombos.addAll(combosWithoutX);
//        allCombos.addAll(combosWithX);
//        return allCombos;
//    }

}
