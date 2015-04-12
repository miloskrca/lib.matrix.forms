package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;
import rs.etf.km123247m.Matrix.Handler.PolynomialHandler;
import rs.etf.km123247m.Polynomial.Term;
import rs.etf.km123247m.PropertyManager.PropertyManager;

/**
 * Created by Miloš Krsmanović.
 * Jan 2015
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class EJMLPolynomialHandler implements PolynomialHandler {

    /**
     * Tolerance used when converting to rational
     */
    private double tolerance = 1.0E-6;

    /**
     * Flag that decides if we should convert to rational
     */
    private boolean convertToRational = false;

    /**
     * Constructor
     */
    public EJMLPolynomialHandler() {
        String toleranceProperty = PropertyManager.getProperty("convert_to_rational_tolerance");
        if(toleranceProperty != null) {
            tolerance = Double.parseDouble(toleranceProperty);
        }
        String convertToRationalProperty = PropertyManager.getProperty("convert_to_rational");
        if(convertToRationalProperty != null) {
            convertToRational = !convertToRationalProperty.equals("0");
        }
    }

    @Override
    public Object[] findRoots(double[] coefficients) {
        int N = coefficients.length - 1;

        // Construct the companion matrix
        DenseMatrix64F denseMatrix64F = new DenseMatrix64F(N, N);

        double a = coefficients[N];
        for (int i = 0; i < N; i++) {
            denseMatrix64F.set(i, N - 1, -coefficients[i] / a);
        }
        for (int i = 1; i < N; i++) {
            denseMatrix64F.set(i, i - 1, 1);
        }

        // use generalized eigenvalue decomposition to find the roots
        EigenDecomposition<DenseMatrix64F> evd = DecompositionFactory.eig(N, false);

        evd.decompose(denseMatrix64F);

        Complex64F[] roots = new Complex64F[N];

        for (int i = 0; i < N; i++) {
            roots[i] = evd.getEigenvalue(i);
        }

        return roots;
    }

    @Override
    public String rootsToFactors(Object[] roots) {
        String string = "";

        for (Object root : roots) {
            Complex64F cRoot = (Complex64F) root;
            if (cRoot.getReal() == 0.0 && cRoot.getImaginary() == 0.0) {
                string += Term.X;
            } else if (cRoot.getReal() == 0.0) {
                string += "(" + Term.X + "+" + toRational(cRoot.getImaginary() * -1.0) + "*i)";
            } else if (cRoot.getImaginary() == 0.0) {
                string += "(" + Term.X + "+" + toRational(cRoot.getReal() * -1.0) + ")";
            } else {
                string += "(" + Term.X + "-(" + toRational(cRoot.getReal()) + "+" + toRational(cRoot.getImaginary()) + "*i))";
            }
        }

        // add * between brackets, add space after/before + and -, replace +- with -
        string = string.replace(")(", ")*(").replace("+-", "-");

        return string;
    }


    public Object[] toRational(Object[] roots) throws Exception {
        if(!convertToRational) {
            return roots;
        }
        String[] sRoots = new String[roots.length];
        for (int i = 0; i < roots.length; i++) {
            Complex64F cRoot = (Complex64F) roots[i];
            if (cRoot.getReal() == 0.0 && cRoot.getImaginary() == 0.0) {
                throw new Exception("?");
            } else if (cRoot.getReal() == 0.0) {
                sRoots[i] = toRational(cRoot.getImaginary()) + "*i";
            } else if (cRoot.getImaginary() == 0.0) {
                sRoots[i] = toRational(cRoot.getReal());
            } else {
                sRoots[i] = toRational(cRoot.getReal()) + "+" + toRational(cRoot.getImaginary()) + "*i";
            }
            sRoots[i] = sRoots[i].replace("+-", "-");
        }
        return sRoots;
    }

    public String toRational(double number) {
        if(!convertToRational) {
            return String.valueOf(number);
        }
        double fix = 1.0;
        if (number < 0) {
            number = number * -1.0;
            fix = -1.;
        }

        double h1 = 1;
        double h2 = 0;
        double k1 = 0;
        double k2 = 1;
        double b = number;
        do {
            double a = Math.floor(b);
            double aux = h1;
            h1 = a * h1 + h2;
            h2 = aux;
            aux = k1;
            k1 = a * k1 + k2;
            k2 = aux;
            b = 1 / (b - a);
        } while (Math.abs(number - h1 / k1) > number * tolerance);

        return (int) (fix * h1) + "/" + (int) k1;
    }
}

