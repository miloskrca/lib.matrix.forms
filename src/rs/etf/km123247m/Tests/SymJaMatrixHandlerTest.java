package rs.etf.km123247m.Tests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Parser.MatrixParser.SymJa.IExprMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;

public class SymJaMatrixHandlerTest {

    private SymJaMatrixHandler handler;

    @Before
    public void setUp() throws Exception {
        File file = new File("./TestData/HandlerTest/SymJaMatrixHandlerTest.txt");
        IParser parser = new IExprMatrixFileParser(file);
        IMatrix matrix = (ArrayMatrix) parser.parseInput();
        handler = new SymJaMatrixHandler(matrix);
    }

    @Test
    public void testAddElements() throws Exception {
        //TODO: Write test
    }

    @Test
    public void testMultiplyElements() throws Exception {
        //TODO: Write test
    }

    @Test
    public void testDivideElements() throws Exception {
        //TODO: Write test
    }

    @Test
    public void testCalculateNegativeElement() throws Exception {
        //TODO: Write test
    }

    @Test
    public void testGetLeadingCoefficientOfElement() throws Exception {
        String coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(0, 0)).toString();
        assert "2".equals(coefficient);

        coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(0, 1)).toString();
        assert "-1".equals(coefficient);

        coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(1, 0)).toString();
        assert "-3".equals(coefficient);

        coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(1, 1)).toString();
        assert "1".equals(coefficient);
       //TODO: Do even better testing with more complicated test cases
    }
}