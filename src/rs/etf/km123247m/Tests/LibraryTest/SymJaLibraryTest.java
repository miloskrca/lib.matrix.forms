package rs.etf.km123247m.Tests.LibraryTest;

/*
import edu.jas.kern.ComputerThreads;
import org.junit.Test;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;

import static org.matheclipse.core.expression.F.*;
*/

import org.junit.Test;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class SymJaLibraryTest {

    @Test
    public void doSomeTests() throws Exception {
        F.initSymbols(null);

        assertValue("Plus[Power[x, 2], Times[x, Times[-1, x]]]", "0");
        assertValue("Plus[Power[x, 3], Times[-1, x, Plus[x, Power[x, 2]]]]", "-x^2");
        assertValue("PolynomialQuotientRemainder[Plus[1, x], x]", "{1,1}");
        assertValue("Plus[Power[x, 3], Times[-1, x, Plus[x, Power[x, 2]]]]", "-x^2");

    }

    protected void assertValue(String input, String expectedOutput) throws Exception {
        StringBufferWriter buf = new StringBufferWriter();
        EvalUtilities util = new EvalUtilities();
        IExpr result = util.evaluate("Expand[" + input + "]");
        OutputFormFactory.get().convert(buf, result);
        String output = buf.toString();
        assert output.equals(expectedOutput);
    }

}
