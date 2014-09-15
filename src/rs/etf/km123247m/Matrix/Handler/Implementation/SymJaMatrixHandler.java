package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.Symbol;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.reflection.system.PolynomialQuotientRemainder;
import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.IMatrix;

import java.util.ArrayList;

/**
 * Implementation of matrix operations using SymJa library
 *
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Handler.Implementation.SymJa
 */
public class SymJaMatrixHandler extends PolynomialMatrixHandler {

    private EvalUtilities util = new EvalUtilities();

    public SymJaMatrixHandler(IMatrix matrix) {
        super(matrix);
//        F.initSymbols(null);
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
        IExpr results = evaluate("PolynomialQuotientRemainder[" + element1.toString() + "," + element2.toString() + ", x]");
        // If the method is successful it will return [List, quotient, remainder],
        // if not it will return [PolynomialQuotientRemainder, element1, element2]
        // if the function is successful and the quotient is not zero
        if(results.isList() && !isZeroElement(results.getAt(1))) {
            results = evaluate(results.getAt(1));
        } else {
            //the result will just be the expression build from elements
            results = evaluate(PolynomialQuotientRemainder.quotientRemainder((IExpr)element1, (IExpr)element2, new Symbol("x"))[0]);
        }

        return results;
    }

    public Object divideCellElementsAndReturnRemainder(Object element1, Object element2) throws Exception {
        IExpr results = evaluate("PolynomialQuotientRemainder[" + element1.toString() + "," + element2.toString() + ", x]");
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
        return ((IExpr)element1).compareTo((IExpr)element2);
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

    // TODO: Test
    @Override
    public boolean hasElementWithPower(Object element, int power) throws Exception {
        ArrayList<CoefficientPowerPair> pairs = getCoefficientPowerPairs(element);
        IExpr oPower = util.evaluate(String.valueOf(power));
        for(CoefficientPowerPair pair: pairs) {
            if(compare(pair.getPower(), oPower) == 0) {
                return true;
            }
        }
        return false;
    }

    // TODO: Test
    @Override
    public Object getCoefficientForPower(Object element, int power) throws Exception {
        ArrayList<CoefficientPowerPair> pairs = getCoefficientPowerPairs(element);
        IExpr oPower = util.evaluate(String.valueOf(power));
        for(CoefficientPowerPair pair: pairs) {
            if(compare(pair.getPower(), oPower) == 0) {
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
        if(element.isNumber()) {
            pairs.add(new CoefficientPowerPair(
                element,
                zero
            ));
        } else if(element.leaves() != null) {
            if(element.isPlus()) {
                for(IExpr leaf: element.leaves()) {
                    pairs.add(getLeadingCoefficientAndPowerOfElementRecursive(leaf));
                }
            } else if(element.isTimes()) {
                if(element.leaves().size() > 2) {
                    for(IExpr leaf: element.leaves()) {
                        pairs.add(getLeadingCoefficientAndPowerOfElementRecursive(leaf));
                    }
                } else {
                    IExpr firstLeaf = element.getAt(1);
                    IExpr secondLeaf = element.getAt(2);
                    CoefficientPowerPair tempCoefficientPowerPair = new CoefficientPowerPair();
                    if(secondLeaf.leaves() != null) {
                        if(secondLeaf.isPower()) {
                            tempCoefficientPowerPair.setPower(secondLeaf.getAt(2));
                        } else {
                            throw new Exception("Fix this2!");
                        }
                    } else if (secondLeaf.isSymbol()) {
                        tempCoefficientPowerPair.setPower(one);
                    } else {
                        throw new Exception("Fix this3!");
                    }
                    if(firstLeaf.isNumber()) {
                        tempCoefficientPowerPair.setCoefficient(firstLeaf);
                    } else if(firstLeaf.leaves().size() > 1) {
                        for(IExpr leaf: element.leaves()) {
                            pairs.add(getLeadingCoefficientAndPowerOfElementRecursive(leaf));
                        }
                    } else {
                        throw new Exception("Fix this4!");
                    }
                    pairs.add(tempCoefficientPowerPair);
                }
            } else if(element.isPower()) {
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
        for(CoefficientPowerPair pair: pairs) {
           if(highestPower.compareTo((IExpr) pair.getPower()) == -1) {
               highestPower = (IExpr) pair.getPower();
               coefficient = (IExpr) pair.getCoefficient();
           } else if(coefficient == null) {
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

    public int getHighestPower(Object expr) throws Exception {

        int power;
        String exprString = expr.toString().replace(" ", "");

        if(isNumeric(exprString)) {
            power = 0;
        } else {
            ArrayList<CoefficientPowerPair> pairs = getCoefficientPowerPairs(evaluate(expr));
            IExpr highestPower = util.evaluate("-1");
            for(CoefficientPowerPair pair: pairs) {
                if(highestPower.compareTo((IExpr) pair.getPower()) == -1) {
                    highestPower = (IExpr) pair.getPower();
                }
            }
            power = Integer.parseInt(highestPower.toString());

//            Pattern p = Pattern.compile("Power\\[(.+),(.+)\\]");
//            Matcher m = p.matcher(exprString);
//
//            while(m.find()) {
//                int groupPower = Integer.parseInt(m.group(2));
//                if(groupPower > power) {
//                    power = groupPower;
//                }
//            }
        }

        return power;
    }

    @Override
    public Object factor(Object element) throws Exception {
        // using util.evaluate instead of evaluate() because we
        // want to avoid Expand[]
        return util.evaluate("Factor[" + element.toString() + "]");
    }

    private boolean isNumeric(String s) {
        return s.matches(("^([\\+\\-]?\\d+)$")) || isDouble(s) || isFraction(s);
    }

    private boolean isFraction(String s) {
        return s.matches(("^([\\+\\-]?\\d+)/([\\+\\-]?\\d+)$"));
    }

    private boolean isDouble(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

}