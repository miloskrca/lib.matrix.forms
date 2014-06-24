package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Polynomial.Polynomial;

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
    public boolean isZeroElement(Object element) {
        return Polynomial.getZeroPolynomial().toString().equals(element.toString());
    }

    @Override
    protected Object getLeadingCoefficientOfElement(Object element) throws Exception {
        return getLeadingCoefficientOfElementRecursive((IExpr) element)[0];
    }

    protected Object[] getLeadingCoefficientOfElementRecursive(IExpr element) throws Exception {
        //TODO: make this function prettier
        IExpr lowestCoefficient = util.evaluate("1");
        int lowestPower = 9999;
        ArrayList<Object[]> pairs = new ArrayList<Object[]>();
        if(element.isNumber()) {
            lowestCoefficient = element;
            lowestPower = 1;
        } else if(element.leaves() != null) {
            String method = element.getAt(0).toString();
            if(method.equals("Plus")) {
                for(IExpr leaf: element.leaves()) {
                    pairs.add(getLeadingCoefficientOfElementRecursive(leaf));
                }
            } else if(method.equals("Times")) {
                Object[] tempArray = new Object[2];
                if(element.getAt(2).leaves() != null) {
                    String method2 = element.getAt(2).getAt(0).toString();
                    if(method2.equals("Power")) {
                        tempArray[1] = element.getAt(2).getAt(2);
                    }
                } else if (element.getAt(2).isSymbol()) {
                    tempArray[1] = lowestCoefficient;
                }
                if(element.getAt(1).isNumber()) {
                    tempArray[0] = element.getAt(1);
                }
                pairs.add(tempArray);
            }
        }
        for(Object[] pair: pairs) {
           if(lowestPower > Integer.parseInt(pair[1].toString())) {
               lowestPower = Integer.parseInt(pair[1].toString());
               lowestCoefficient = (IExpr) pair[0];
           }
        }
        return new Object[] {
            lowestCoefficient,
            lowestPower
        };
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
}