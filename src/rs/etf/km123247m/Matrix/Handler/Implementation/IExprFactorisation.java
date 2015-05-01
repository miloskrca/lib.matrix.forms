package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Polynomial.Term;

import java.util.*;

/**
 * Created by Miloš Krsmanovi?.
 * Apr 2015
 * <p/>
 * package: rs.etf.km123247m.Parser.MatrixParser.SymJa
 */
public class IExprFactorisation {

    /**
     * Util
     */
    private EvalUtilities util = new EvalUtilities();

    /**
     * Handler
     */
    private final MatrixHandler handler;

    /**
     * Factors
     */
    private ArrayList<Object> factors = new ArrayList<Object>();

    /**
     * Roots
     */
    private ArrayList<Object> roots = new ArrayList<Object>();

    /**
     * Is factorisation correct
     */
    private boolean factorisationCorrect;

    public IExprFactorisation(MatrixHandler handler) {
        this.handler = handler;
    }

    public ArrayList<Object> generateRoots(Object poly) throws Exception {
        IExpr solved = util.evaluate("Roots[" + poly + "]");
        ArrayList<Object> roots = new ArrayList<Object>();
        for (IExpr leaf : solved.leaves()) {
            roots.add(util.evaluate(leaf));
        }

        return roots;
    }

    /**
     * Do factorisation and save roots and factors
     *
     * @param poly Polynomial
     *
     * @return If the factorisation was successful
     * (returned boolean is not always correct, sometimes the library cannot confirm correct factorisation)
     *
     * @throws Exception
     */
    public boolean factorize(Object poly) throws Exception {
        int rootNum = handler.getHighestPower(poly);
        ArrayList<Object> roots = generateRoots(poly);
        String tempPolyBase = "";
        for (Object f : getFactorsFromRoots(roots)) {
            tempPolyBase += "*(" + f.toString() + ")";
        }
        tempPolyBase = tempPolyBase.substring(1);

        if (rootNum > roots.size()) {
            for (Set<Object> setOfRootCombinations : combinations(roots, rootNum - roots.size())) {
                String setOfFactorCombinationsString = tempPolyBase;
                for (Object factorCombinationObject : getFactorsFromRoots(setOfRootCombinations)) {
                    setOfFactorCombinationsString += "*(" + factorCombinationObject + ")";
                }
                Object tempPolyO = handler.getObjectFromString(setOfFactorCombinationsString);
                tempPolyO = util.evaluate("Expand[" + tempPolyO.toString() + "]");
                if (handler.compare(tempPolyO, poly) == 0) {
                    tempPolyBase = setOfFactorCombinationsString;
                    roots.addAll(setOfRootCombinations);
                    // sort roots
                    Collections.sort(roots, new Comparator<Object>() {
                        public int compare(Object root1, Object root2) {
                            return root1.toString().compareTo(root2.toString());
                        }
                    });
                }
            }
        }

        this.roots.addAll(roots);
        this.factors.addAll(getFactorsFromRoots(roots));

        Object tempPolyO = handler.getObjectFromString(tempPolyBase);
        tempPolyO = util.evaluate("Expand[" + tempPolyO.toString() + "]");

        this.factorisationCorrect = handler.compare(tempPolyO, poly) == 0;

        return this.factorisationCorrect;
    }

    /**
     * Gets all unique combinations for a list of Objects
     *
     * @param objectList List of objects
     * @param combinationSize Number of objects in every combination
     *
     * @return A set of all unique combinations
     */
    public Set<Set<Object>> combinations(List<Object> objectList, int combinationSize) {

        Set<Set<Object>> allCombos = new HashSet<Set<Object>>();
        // base cases for recursion
        if (combinationSize == 0) {
            // There is only one combination of size 0, the empty team.
            allCombos.add(new HashSet<Object>());
            return allCombos;
        }
        if (combinationSize > objectList.size()) {
            // There can be no teams with size larger than the group size,
            // so return allCombos without putting any teams in it.
            return allCombos;
        }

        // Create a copy of the group with one item removed.
        List<Object> groupWithoutX = new ArrayList<Object>(objectList);
        Object x = groupWithoutX.remove(groupWithoutX.size() - 1);

        Set<Set<Object>> combosWithoutX = combinations(groupWithoutX, combinationSize);
        Set<Set<Object>> combosWithX = combinations(groupWithoutX, combinationSize - 1);
        for (Set<Object> combo : combosWithX) {
            combo.add(x);
        }
        allCombos.addAll(combosWithoutX);
        allCombos.addAll(combosWithX);
        return allCombos;
    }

    /**
     * Generates a list of factors from a list of roots
     *
     * @param roots A list of roots
     *
     * @return A list of factors
     * @throws Exception
     */
    protected ArrayList<Object> getFactorsFromRoots(ArrayList<Object> roots) throws Exception {
        ArrayList<Object> factors = new ArrayList<Object>();
        for (Object r : roots) {
            String sFactor;
            if (handler.isZeroElement(r)) {
                sFactor = String.valueOf(Term.X);
            } else {
                sFactor = "x-(" + r.toString() + ")";
            }
            factors.add(handler.getObjectFromString(sFactor));
        }

        return factors;
    }

    /**
     * Generates a list of factors from a set of roots
     *
     * @param roots A set of roots
     *
     * @return A list of factors
     * @throws Exception
     */
    protected ArrayList<Object> getFactorsFromRoots(Set<Object> roots) throws Exception {
        return getFactorsFromRoots(new ArrayList<Object>(roots));
    }

    /**
     * Resets roots and factors
     */
    public void resetRootsAndFactors() {
        this.roots = new ArrayList<Object>();
        this.factors = new ArrayList<Object>();
        this.factorisationCorrect = true;
    }

    /**
     * Get factors
     *
     * @return Calculated factors
     */
    public ArrayList<Object> getFactors() {
        return factors;
    }

    /**
     * Get roots
     *
     * @return Calculated roots
     */
    public ArrayList<Object> getRoots() {
        return roots;
    }

    /**
     * Is factorisation correct
     *
     * @return If factorisation is correct
     */
    public boolean isFactorisationCorrect() {
        return factorisationCorrect;
    }
}
