package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.Symbol;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.reflection.system.PolynomialQuotientRemainder;
import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Polynomial.Term;
import java.util.ArrayList;

/**
 * Implementation of matrix operations using SymJa library
 * <p/>
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Handler.Implementation.SymJa
 */
public class SymJaMatrixHandler extends PolynomialMatrixHandler {

    private EvalUtilities util = new EvalUtilities();
    private IExprFactorisation iExprFactorisation;

    public SymJaMatrixHandler(IMatrix matrix) {
        super(matrix);
//        F.initSymbols(null);
    }

    @Override
    protected boolean isElementSymbol(Object element) {
        return !isWholeNumeric(element.toString());
//        return element.toString().contains(String.valueOf(Term.X));
    }

    @Override
    protected Object getElementEquivalentToOne() throws Exception {
        return util.evaluate("1");
    }

    @Override
    protected Object getElementEquivalentToZero() throws Exception {
        return util.evaluate("0");
    }

    @Override
    protected Object addElements(Object element1, Object element2) throws Exception {
        return evaluate(((IExpr) element1).plus((IExpr) element2));
    }

    @Override
    protected Object multiplyElements(Object element1, Object element2) throws Exception {
        return evaluate(((IExpr) element1).multiply((IExpr) element2));
    }

    @Override
    protected Object divideElements(Object element1, Object element2) throws Exception {
        IExpr results = evaluate("PolynomialQuotientRemainder[" + element1.toString() + "," + element2.toString() + ", " + Term.X + "]");
        // If the method is successful it will return [List, quotient, remainder],
        // if not it will return [PolynomialQuotientRemainder, element1, element2]
        // if the function is successful and the quotient is not zero
        if (results.isList() && !isZeroElement(results.getAt(1))) {
            results = evaluate(results.getAt(1));
        } else {
            //the result will just be the expression build from elements
            results = evaluate(PolynomialQuotientRemainder.quotientRemainder((IExpr) element1, (IExpr) element2, new Symbol(String.valueOf(Term.X)))[0]);
        }

        return results;
    }

    public Object divideCellElementsAndReturnRemainder(Object element1, Object element2) throws Exception {
        IExpr results = evaluate("PolynomialQuotientRemainder[" + element1.toString() + "," + element2.toString() + ", " + Term.X + "]");
        return evaluate(results.getAt(2));
//        IExpr[] results = PolynomialQuotientRemainder.quotientRemainder((IExpr) element1, (IExpr) element2, new Symbol("x"));
//        return evaluate(results[1]);
    }

    @Override
    public Object calculateNegativeElement(Object element) throws Exception {
        return evaluate(((IExpr) element).negate());
    }

    @Override
    public int comparePowersOfElements(Object element1, Object element2) throws Exception {
        int power1 = getHighestPower(element1);
        int power2 = getHighestPower(element2);
        return power1 > power2 ? 1 : (power1 < power2 ? -1 : 0);
    }

    @Override
    public boolean isZeroElement(Object element) throws Exception {
        int result = (util.evaluate(element.toString())).compareTo((IExpr) getElementEquivalentToZero());
        return result == 0;
//        return getElementEquivalentToZero().toString().equals(element.toString());
    }

    @Override
    public int compare(Object element1, Object element2) {
        return ((IExpr) element1).compareTo((IExpr) element2);
    }

    @Override
    public Object getObjectFromString(String string) throws Exception {
        return util.evaluate(String.valueOf(string));
    }

    @Override
    public boolean isElementDividing(Object element1, Object element2) throws Exception {
        Object remainder = divideCellElementsAndReturnRemainder(element1, element2);
        Object quotient = divideCellElements(element1, element2);
        return !isZeroElement(quotient) && isZeroElement(remainder);
    }

    @Override
    protected Object getLeadingCoefficientOfElement(Object element) throws Exception {
        return getLeadingCoefficientAndPowerOfElementRecursive((IExpr) element).getCoefficient();
    }

    @Override
    public boolean hasElementWithPower(Object element, int power) throws Exception {
        ArrayList<CoefficientPowerPair> pairs = getCoefficientPowerPairs(element);
        IExpr oPower = util.evaluate(String.valueOf(power));
        for (CoefficientPowerPair pair : pairs) {
            if (compare(pair.getPower(), oPower) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getCoefficientForPower(Object element, int power) throws Exception {
        ArrayList<CoefficientPowerPair> pairs = getCoefficientPowerPairs(element);
        IExpr oPower = util.evaluate(String.valueOf(power));
        for (CoefficientPowerPair pair : pairs) {
            if (compare(pair.getPower(), oPower) == 0) {
                return pair.getCoefficient();
            }
        }

        return getElementEquivalentToZero();
    }

    /**
     * Creates a list of Coefficient-Power pairs for the given polynomial.
     *
     * @param elementObject Given polynomial
     * @return Returns a list of Coefficient-Power pairs.
     * @throws Exception
     */
    public ArrayList<CoefficientPowerPair> getCoefficientPowerPairs(Object elementObject) throws Exception {
        IExpr element = (IExpr) elementObject;
        IExpr zero = (IExpr) getElementEquivalentToZero();
        IExpr one = (IExpr) getElementEquivalentToOne();
        ArrayList<CoefficientPowerPair> pairs = new ArrayList<CoefficientPowerPair>();
        if (element.isNumber()) {
            pairs.add(new CoefficientPowerPair(
                    element,
                    zero
            ));
        } else if (element.leaves() != null) {
            if (element.isPlus()) {
                for (IExpr leaf : element.leaves()) {
                    pairs.add(getLeadingCoefficientAndPowerOfElementRecursive(leaf));
                }
            } else if (element.isTimes()) {
                if (element.leaves().size() > 2) {
                    for (IExpr leaf : element.leaves()) {
                        pairs.add(getLeadingCoefficientAndPowerOfElementRecursive(leaf));
                    }
                } else {
                    IExpr firstLeaf = element.getAt(1);
                    IExpr secondLeaf = element.getAt(2);
                    CoefficientPowerPair tempCoefficientPowerPair = new CoefficientPowerPair();
                    if (secondLeaf.leaves() != null) {
                        if (secondLeaf.isPower()) {
                            tempCoefficientPowerPair.setPower(secondLeaf.getAt(2));
                        } else {
                            throw new Exception("Fix this2!");
                        }
                    } else if (secondLeaf.isSymbol()) {
                        tempCoefficientPowerPair.setPower(one);
                    } else {
                        throw new Exception("Fix this3!");
                    }
                    if (firstLeaf.isNumber()) {
                        tempCoefficientPowerPair.setCoefficient(firstLeaf);
                    } else if (firstLeaf.leaves().size() > 1) {
                        for (IExpr leaf : element.leaves()) {
                            pairs.add(getLeadingCoefficientAndPowerOfElementRecursive(leaf));
                        }
                    } else {
                        throw new Exception("Fix this4!");
                    }
                    pairs.add(tempCoefficientPowerPair);
                }
            } else if (element.isPower()) {
                pairs.add(new CoefficientPowerPair(
                        one,
                        element.getAt(2)
                ));
            }
        } else if (element.isSymbol()) {
            pairs.add(new CoefficientPowerPair(one, one));
        }

        return pairs;
    }

    protected CoefficientPowerPair getLeadingCoefficientAndPowerOfElementRecursive(IExpr element) throws Exception {
        ArrayList<CoefficientPowerPair> pairs = getCoefficientPowerPairs(element);
        IExpr coefficient = null;
        IExpr highestPower = util.evaluate("-1");
        for (CoefficientPowerPair pair : pairs) {
            if (highestPower.compareTo((IExpr) pair.getPower()) == -1) {
                highestPower = (IExpr) pair.getPower();
                coefficient = (IExpr) pair.getCoefficient();
            } else if (coefficient == null) {
                coefficient = (IExpr) pair.getCoefficient();
            }
        }

        return new CoefficientPowerPair(coefficient, highestPower);
    }

    protected IExpr evaluate(Object expr) throws Exception {
        IExpr result;
        try {
            result = util.evaluate("Expand[" + expr.toString() + "]");
        } catch (Exception exception) {
            result = util.evaluate(expr.toString());
        }
        return result;
    }

    /**
     * Recursively find the highest power
     *
     * @param expr IExpr
     * @return Highest power
     */
    public int getHighestPower(Object expr) throws Exception {
        IExpr poly = evaluate(expr);
        int polyPower = 0;
        if (poly.isNumber()) {
            polyPower = 0;
        } else if (poly.isSymbol()) {
            polyPower = 1;
        } else if (poly.isPower()) {
            for (IExpr subLeaf : poly.leaves()) {
                if (subLeaf.leaves() == null && !subLeaf.isSymbol() && isWholeNumeric(subLeaf.toString())) {
                    polyPower = Integer.parseInt(subLeaf.toString());
                }
            }
        } else if (poly.leaves() != null) {
            for (IExpr leaf : poly.leaves()) {
                int temp = getHighestPower(leaf);
                if (temp > polyPower) {
                    polyPower = temp;
                }
            }
        }

        return polyPower;
    }

    @Override
    public ArrayList<Object> factor(Object element) throws Exception {
        Object basicFactors = util.evaluate("Factor[" + element + "]");
        if (!element.equals(basicFactors.toString()) && !basicFactors.toString().contains(Term.X + "^")) {
            getIExprFactorisation(true);
            return getFactorsFromElement(basicFactors);
        }

        IExprFactorisation f = getIExprFactorisation(true);
        f.factorize(element);
        if (!f.isFactorisationCorrect()) {
            System.out.println("Warning: SymJa couldn't confirm successful factorisation for polynomial: " + element);
        }
        return f.getFactors();
    }

    @Override
    public ArrayList<Object> getRoots(Object element) throws Exception {
        IExprFactorisation f = getIExprFactorisation(true);
        f.factorize(element);
        if (!f.isFactorisationCorrect()) {
            System.out.println("Warning: SymJa couldn't confirm successful factorisation for polynomial: " + element);
        }
        return f.getRoots();
    }

    @Override
    public ArrayList<Object> getFactorsFromElement(Object element) throws Exception {
        ArrayList<Object> factors = new ArrayList<Object>();
        String elementString = element.toString();
        String[] factorsString = elementString.split("\\*\\(");

        for (String f : factorsString) {
            if (f.charAt(0) != '(' && f.charAt(f.length() - 1) == ')') {
                f = f.replace(")", "");
            }
            if (f.contains(")^") && f.charAt(0) != '(') {
                f = "(" + f;
            }
            IExpr factor = (IExpr) getObjectFromString(f);
            if (!factor.isNumber()) {
                factors.add(factor);
            }
        }

        return factors;
    }

    @Override
    public CoefficientPowerPair getCoefficientPowerPairFromFactor(Object factor) throws Exception {
        IExpr element = (IExpr) factor;
        CoefficientPowerPair pair = new CoefficientPowerPair();
        if (element.isPower()) {
            for (IExpr leaf : element.leaves()) {
                if (leaf.isNumber()) {
                    pair.setPower(leaf);
                } else if (leaf.isPlus()) {
                    setFactorCoefficient(pair, leaf);
                } else if (leaf.isSymbol()) {
                    pair.setCoefficient(getZero());
                } else {
                    throw new Exception("Un-parsable factor!");
                }
            }
        } else if (element.isPlus()) {
            pair.setPower(getOne());
            setFactorCoefficient(pair, element);
        } else if (element.isSymbol()) {
            pair.setPower(getOne());
            pair.setCoefficient(getZero());
        } else {
            throw new Exception("Un-parsable factor!");
        }

        return pair;
    }

    @Override
    /**
     * @return Array with two roots
     */
    public Object[] quadraticFormula(Object element) throws Exception {
        int highestPower = getHighestPower(element);
        if (highestPower != 2) {
            throw new Exception("sad");
        }

        Object a = getCoefficientForPower(element, 2);
        Object b = getCoefficientForPower(element, 1);
        Object c = getCoefficientForPower(element, 0);

        // ((b)*(-1) + ((b)^2 - 4*(a)*(c))^(1/2))/(2*(a));
        String string1 = "((" + b + ")*(-1) + ((" + b + ")^2 - 4*(" + a + ")*(" + c + "))^(1/2))" + "/" + "(2*(" + a + "))";
        // ((b)*(-1) - ((b)^2 - 4*(a)*(c))^(1/2))/(2*(a));
        String string2 = "((" + b + ")*(-1) - ((" + b + ")^2 - 4*(" + a + ")*(" + c + "))^(1/2))" + "/" + "(2*(" + a + "))";

        return new Object[]{evaluate(string1), evaluate(string2)};
    }

    @Override
    public String getFactorsStringFromRoots(Object[] roots) throws Exception {
        String factors = "";
        for (Object root : roots) {
            factors += "*(" + (getObjectFromString(Term.X + "- (" + root.toString() + ")")).toString() + ")";
        }

        return factors.replace("+-", "-").substring(1);
    }

    protected void setFactorCoefficient(CoefficientPowerPair pair, IExpr leaf) throws Exception {
        Object leftoverCoefficient = null;
        for (IExpr plusLeaf : leaf.leaves()) {
            if (plusLeaf.isNumber()) {
                // set found coefficient
                pair.setCoefficient(plusLeaf);
            } else if (plusLeaf.isTimes()) {
                // if we found times than the factoring didn't go as planned
                // we got something like (a*x-b)^n instead of (x-b)^n
                // so we need to fix the factor like (x-b/a)^n
                for (IExpr timesLeaf : plusLeaf.leaves()) {
                    if (timesLeaf.isNumber()) {
                        // we remember the faulty factor
                        leftoverCoefficient = timesLeaf;
                    }
                }
            } else if (!plusLeaf.isSymbol()) {
                throw new Exception("Un-parsable factor!");
            }
        }
        // faulty factor found, we fix it here
        if (leftoverCoefficient != null) {
            pair.setCoefficient(
                    divideElements(pair.getCoefficient(), leftoverCoefficient)
            );
        }
    }

    public IExprFactorisation getIExprFactorisation() {
        return getIExprFactorisation(false);
    }

    public IExprFactorisation getIExprFactorisation(boolean reset) {
        if (this.iExprFactorisation == null) {
            this.iExprFactorisation = new IExprFactorisation(this);
        }
        if (reset) {
            this.iExprFactorisation.resetRootsAndFactors();
        }
        return this.iExprFactorisation;
    }
}