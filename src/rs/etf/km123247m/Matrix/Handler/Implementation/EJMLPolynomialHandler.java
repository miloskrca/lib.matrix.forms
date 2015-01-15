package rs.etf.km123247m.Matrix.Handler.Implementation;

import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;
import rs.etf.km123247m.Matrix.Handler.PolynomialHandler;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;
import rs.etf.km123247m.Polynomial.Term;

/**
 * Created by Miloš Krsmanović.
 * Jan 2015
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class EJMLPolynomialHandler implements PolynomialHandler {

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

    public Object[] formatRoots(Object[] roots) {
        String[] strings = new String[roots.length];

        for (int i = 0; i < roots.length; i++) {
            Object root = roots[i];
            Complex64F cRoot = (Complex64F) root;
            if (cRoot.getReal() == 0.0 && cRoot.getImaginary() == 0.0) {
                strings[i] = String.valueOf(Term.X);
            } else if (cRoot.getReal() == 0.0) {
                strings[i] = (cRoot.getImaginary() * -1.0) + "*i";
            } else if (cRoot.getImaginary() == 0.0) {
                strings[i] = (cRoot.getReal() * -1.0) + "";
            } else {
                strings[i] = cRoot.getReal() + "+" + cRoot.getImaginary() + "*i";
            }
            // add * between brackets, add space after/before + and -, replace +- with -
            strings[i] = strings[i].replace("+-", "-");
        }


        return strings;
    }

    @Override
    public String mergeRoots(Object[] roots) {
        String string = "";

        for (Object root : roots) {
            Complex64F cRoot = (Complex64F) root;
            if (cRoot.getReal() == 0.0 && cRoot.getImaginary() == 0.0) {
                string += Term.X;
            } else if (cRoot.getReal() == 0.0) {
                string += "(" + Term.X + "+" + (cRoot.getImaginary() * -1.0) + "*i)";
            } else if (cRoot.getImaginary() == 0.0) {
                string += "(" + Term.X + "+" + (cRoot.getReal() * -1.0) + ")";
            } else {
                string += "(" + Term.X + "-(" + cRoot.getReal() + "+" + cRoot.getImaginary() + "*i))";
            }
        }

        // add * between brackets, add space after/before + and -, replace +- with -
        string = string.replace(")(", ")*(").replace("+-", "-");

        return string;
    }
}

