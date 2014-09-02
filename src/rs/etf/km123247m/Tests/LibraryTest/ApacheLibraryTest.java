package rs.etf.km123247m.Tests.LibraryTest;


import org.junit.Before;
import org.junit.Test;
import org.mathIT.algebra.Polynomial;
import rs.etf.km123247m.Matrix.Handler.Implementation.ApacheMatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Parser.MatrixParser.Apache.ApacheMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;


/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.Tests
 */
public class ApacheLibraryTest {

    private ApacheMatrixHandler handler;

    @Before
    public void setUp() throws Exception {
        File file = new File("./TestData/HandlerTest/ApacheMatrixHandlerTest.txt");
        IParser parser = new ApacheMatrixFileParser(file);
        IMatrix matrix = (ArrayMatrix) parser.parseInput();
        handler = new ApacheMatrixHandler(matrix);
    }

    @Test
    public void doSomeTests() throws Exception {
        Polynomial p1 = (Polynomial) handler.getMatrix().get(0, 0).getElement();
        Polynomial p2 = (Polynomial) handler.getMatrix().get(1, 1).getElement();
        System.out.println(p2.divide(p1)[0]);
        System.out.println(p2.divide(p1)[1]);
    }

}
