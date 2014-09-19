package rs.etf.km123247m.Tests.LibraryTest;


import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Handler.Implementation.MathITMatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Parser.MatrixParser.MathIT.MathITMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;
import rs.etf.km123247m.Polynomial.MathITPolynomial;

import java.io.File;
import java.util.HashMap;


/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class MathITLibraryTest {

    private MathITMatrixHandler handler;

    @Before
    public void setUp() throws Exception {
        File file = new File("./TestData/HandlerTest/MathITMatrixHandlerTest.txt");
        IParser parser = new MathITMatrixFileParser(file);
        IMatrix matrix = (ArrayMatrix) parser.parseInput();
        handler = new MathITMatrixHandler(matrix);
    }

    @Test
    public void doSomeTests() throws Exception {
        MathITPolynomial p1 = (MathITPolynomial) handler.getMatrix().get(0, 0).getElement();
        MathITPolynomial p2 = (MathITPolynomial) handler.getMatrix().get(1, 1).getElement();
        assert ("5*x^3 -10*x^2 +19*x -37").equals(p2.divide(p1)[0].toString());
        assert ("74").equals(p2.divide(p1)[1].toString());

        IMatrix duplicate = handler.duplicate(handler.getMatrix());
        assert handler.getMatrix().toString().equals(duplicate.toString());

    }

}
