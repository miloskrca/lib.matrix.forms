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

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class SymJaMatrixTest {
/*
    @Test
    public void createSomeMatrices() {
        // Static initialization of the MathEclipse engine instead of null
        // you can set a file name to overload the default initial
        // rules. This step should be called only once at program setup:
        F.initSymbols(null);
        EvalUtilities util = new EvalUtilities();

        IExpr result;

        try {
            StringBufferWriter buf = new StringBufferWriter();
            String input = "{{1, 2}, {x^3, y^3 + y^3}} * {{1, 2}, {2, 1}}";

            IExpr yPlusYsquared = Plus($s("y"), Power($s("y"), 2));
            System.out.println(yPlusYsquared);
            OutputFormFactory.get().convert(buf, yPlusYsquared);
            String output = buf.toString();
            System.out.println(" is " + output);


            String input2 = "{x^3, y^3 + y^3, y} + {1, 2, 2}";
            result = util.evaluate(input);
            OutputFormFactory.get().convert(buf, result);
            output = buf.toString();
            System.out.println("Expanded form for2  " + input2 + " is " + output);

            result = Plus(List(List(Power($s("x"),C3),$s("y")),List(C1,Plus(Power($s("y"),C2),$s("y")))),List(List(Power($s("x"),C3),$s("y")),List(C1,Plus(Power($s("y"),C2),$s("y")))));

//            result = util.evaluate(input);
            OutputFormFactory.get().convert(buf, result);
            output = buf.toString();
            System.out.println("Expanded form for " + input + " is " + output);

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            // Call terminate() only one time at the end of the program
            ComputerThreads.terminate();
        }
    }
    */
}
