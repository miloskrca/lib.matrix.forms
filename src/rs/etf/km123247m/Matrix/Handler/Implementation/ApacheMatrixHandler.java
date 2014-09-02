package rs.etf.km123247m.Matrix.Handler.Implementation;

import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Parser.MatrixParser.Apache.ApacheStringParser;
import rs.etf.km123247m.Polynomial.MathITPolynomial;

import java.util.ArrayList;
import java.util.Map;

/**
 * Implementation of matrix operations using SymJa library
 *
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Handler.Implementation.SymJa
 */
public class ApacheMatrixHandler extends PolynomialMatrixHandler {

    public ApacheMatrixHandler(IMatrix matrix) {
        super(matrix);
    }

    @Override
    public ArrayList<CoefficientPowerPair> getCoefficientPowerPairs(Object element) throws Exception {
        ArrayList<CoefficientPowerPair> pairs = new ArrayList<CoefficientPowerPair>();
        for (Map.Entry<Integer, Double> entry : ((MathITPolynomial) element).entrySet()) {
            pairs.add(new CoefficientPowerPair(entry.getValue(), entry.getKey()));
        }
        return pairs;
    }

    @Override
    protected Object getElementEquivalentToOne() throws Exception {
        MathITPolynomial p = new MathITPolynomial();
        p.put(0, (double) 1);
        return p;
    }

    @Override
    protected Object getElementEquivalentToZero() throws Exception {
        MathITPolynomial p = new MathITPolynomial();
        p.put(0, (double) 0);
        return p;
    }

    protected int getHighestPower(Object element) throws Exception {
        return ((MathITPolynomial)element).deg();
    }

    @Override
    protected Object getLeadingCoefficientOfElement(Object element) throws Exception {
        return ((MathITPolynomial)element).lastEntry().getValue();
    }

    public Object divideCellElementsAndReturnRemainder(Object element1, Object element2) throws Exception {
        return ((MathITPolynomial)element1).divide(((MathITPolynomial)element2))[1];
    }

    @Override
    protected Object addElements(Object element1, Object element2) throws Exception {
        return ((MathITPolynomial)element1).add(((MathITPolynomial) element2));
    }

    @Override
    protected Object multiplyElements(Object element1, Object element2) throws Exception {
        return ((MathITPolynomial)element1).multiply(((MathITPolynomial) element2));
    }

    @Override
    protected Object divideElements(Object element1, Object element2) throws Exception {
        return ((MathITPolynomial)element1).divide(((MathITPolynomial) element2))[0];
    }

    @Override
    public Object calculateNegativeElement(Object element) throws Exception {
        //@TODO: check if zero
        MathITPolynomial negativeOne = new MathITPolynomial();
        negativeOne.put(0, (double) -1);
        return ((MathITPolynomial)element).multiply(negativeOne);
    }

    @Override
    public int comparePowersOfElements(Object element1, Object element2) throws Exception {
        int deg1 = getHighestPower(element1);
        int deg2 = getHighestPower(element2);
        return deg1 > deg2 ? 1 : (deg2 > deg1 ? -1 : 0);
    }

    @Override
    public boolean isZeroElement(Object element) throws Exception {
        //@TODO: this lib can have 0 for all elements so size() is not good enough
        Map.Entry<Integer, Double> entry = ((MathITPolynomial)element).firstEntry();
        return ((MathITPolynomial)element).size() == 1 && entry.getValue() == 0 && entry.getKey() == 0;
    }

    @Override
    public int compare(Object element1, Object element2) {
        return element1.equals(element2) ? 0 : 1;
    }

    @Override
    public Object getObjectFromString(String string) throws Exception {
        ApacheStringParser parser = new ApacheStringParser();
        parser.setInputString(string);
        return parser.parseInput();
    }

    @Override
    public boolean isElementDividing(Object element1, Object element2) throws Exception {
        return compare(((MathITPolynomial)element1).divide(((MathITPolynomial)element2))[1], getElementEquivalentToZero()) == 0;
    }
}