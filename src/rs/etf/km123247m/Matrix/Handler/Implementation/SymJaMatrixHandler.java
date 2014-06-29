package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Matrix.IMatrix;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        F.initSymbols(null);
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
        IExpr results = evaluate("PolynomialQuotientRemainder[" + element1.toString() + "," + element2.toString() + "]");
        // If the method is successful it will return [List, quotient, remainder],
        // if not it will return [PolynomialQuotientRemainder, element1, element2]
        if(results.getAt(0).toString().equals("List")) {
            results = results.getAt(1);
        } else {
            results = evaluate(element1.toString() + " / " + element2.toString());
        }
        return results;
    }

    @Override
    public Object divideCellElementsAndReturnRemainder(Object element1, Object element2) throws Exception {
        IExpr results = evaluate("PolynomialQuotientRemainder[" + element1.toString() + "," + element2.toString() + "]");
        return results.getAt(2);
    }

    @Override
    public Object calculateNegativeElement(Object element) throws Exception {
        return evaluate(((IExpr) element).negate());
    }

    @Override
    public int comparePowersOfElements(Object element1, Object element2) {
        int power1 = getHighestPower(element1);
        int power2 = getHighestPower(element2);
        return power1 > power2 ? 1 : (power1 < power2 ? -1 : 0);
    }

    @Override
    public boolean isZeroElement(Object element) throws Exception {
        return getElementEquivalentToZero().toString().equals(element.toString());
    }

    @Override
    public int compare(Object element1, Object element2) {
        return ((IExpr)element1).compareTo((IExpr)element2);
    }

    @Override
    protected Object getLeadingCoefficientOfElement(Object element) throws Exception {
        return getLeadingCoefficientOfElementRecursive((IExpr) element).getCoefficient();
    }

    protected CoefficientPowerPair getLeadingCoefficientOfElementRecursive(IExpr element) throws Exception {
        IExpr zero = (IExpr) getElementEquivalentToZero();
        IExpr one = (IExpr) getElementEquivalentToOne();
        IExpr lowestPower = util.evaluate("9999");
        ArrayList<CoefficientPowerPair> pairs = new ArrayList<CoefficientPowerPair>();
        if(element.isNumber()) {
            pairs.add(new CoefficientPowerPair(
                element,
                zero
            ));
        } else if(element.leaves() != null) {
            String method = element.getAt(0).toString();
            if(method.equals("Plus")) {
                for(IExpr leaf: element.leaves()) {
                    pairs.add(getLeadingCoefficientOfElementRecursive(leaf));
                }
            } else if(method.equals("Times")) {
                if(element.leaves().size() > 2) {
                    throw new Exception("Fix this!");
                }
                CoefficientPowerPair tempCoefficientPowerPair = new CoefficientPowerPair();
                IExpr firstLeaf = element.getAt(1);
                IExpr secondLeaf = element.getAt(2);
                if(secondLeaf.leaves() != null) {
                    String method2 = secondLeaf.getAt(0).toString();
                    if(method2.equals("Power")) {
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
                } else {
                    throw new Exception("Fix this4!");
                }
                pairs.add(tempCoefficientPowerPair);
            } else if(method.equals("Power")) {
                pairs.add(new CoefficientPowerPair(
                    one,
                    element.getAt(2)
                ));
            }
        } else if (element.isSymbol()) {
            pairs.add(new CoefficientPowerPair(one, one));
        }
        IExpr coefficient = null;
        for(CoefficientPowerPair pair: pairs) {
           if(lowestPower.compareTo(pair.getPower()) == 1) {
               lowestPower = pair.getPower();
               coefficient = pair.getCoefficient();
           } else if(coefficient == null) {
               coefficient = pair.getCoefficient();
           }
        }
        return new CoefficientPowerPair(coefficient, lowestPower);
    }

    protected IExpr evaluate(Object expr) throws Exception {
        return util.evaluate("Expand[" + expr.toString() + "]");
    }

    @Override
    protected int getHighestPower(Object expr) {
        int power = 1;
        String exprString = expr.toString().replace(" ", "");

        if(isNumeric(exprString)) {
            power = 0;
        } else {
            Pattern p = Pattern.compile("Power\\[(.),(\\d+)\\]");
            Matcher m = p.matcher(exprString);

            while(m.find()) {
                int groupPower = Integer.parseInt(m.group(2));
                if(groupPower > power) {
                    power = groupPower;
                }
            }
        }

        return power;
    }

    protected boolean isNumeric(String s) {
        return s.matches(("^([\\+\\-]?\\d+)/([\\+\\-]?\\d+)$")) || s.matches("[-+]?\\d*\\.?\\d+");
    }

    private class CoefficientPowerPair {
        private IExpr power;
        private IExpr coefficient;

        private CoefficientPowerPair() {
            this.power = null;
            this.coefficient = null;
        }

        private CoefficientPowerPair(IExpr coefficient, IExpr power) {
            this.power = power;
            this.coefficient = coefficient;
        }

        public IExpr getPower() {
            return power;
        }

        public void setPower(IExpr power) {
            this.power = power;
        }

        public IExpr getCoefficient() {
            return coefficient;
        }

        public void setCoefficient(IExpr coefficient) {
            this.coefficient = coefficient;
        }
    }
}