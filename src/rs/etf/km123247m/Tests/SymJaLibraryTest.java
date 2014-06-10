package rs.etf.km123247m.Tests;

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

import java.io.IOException;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class SymJaLibraryTest {

    @Test
    public void doSomeTests() {
        F.initSymbols(null);

        try {
            StringBufferWriter buf = new StringBufferWriter();
            String input = "Expand[(3*x+3)*(-1/3*x+1/3)]";
            EvalUtilities util = new EvalUtilities();
            IExpr result = util.evaluate(input);
            OutputFormFactory.get().convert(buf, result);
            String output = buf.toString();
            assert output.equals("-x^2+1");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
