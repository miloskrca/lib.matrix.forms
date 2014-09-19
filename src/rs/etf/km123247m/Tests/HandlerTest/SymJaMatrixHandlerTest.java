package rs.etf.km123247m.Tests.HandlerTest;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Handler.CoefficientPowerPair;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Parser.MatrixParser.SymJa.IExprMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;

/**
 * TODO: Add test cases for most of the methods
 */
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
    public void testGetLeadingCoefficientOfElement() throws Exception {
        String coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(0, 0).getElement()).toString();
        assert "1".equals(coefficient);

        coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(0, 1).getElement()).toString();
        assert "1".equals(coefficient);

        coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(1, 0).getElement()).toString();
        assert "-1".equals(coefficient);

        coefficient = handler.getLeadingCoefficient(handler.getMatrix().get(1, 1).getElement()).toString();
        assert "5".equals(coefficient);
    }

    @Test
    public void testFactors() throws Exception{
        CoefficientPowerPair pair = handler.getCoefficientPowerPairFromFactor(
                handler.getObjectFromString("x^2")
        );
        assert handler.compare(pair.getCoefficient(), handler.getZero()) == 0;
        assert handler.compare(pair.getPower(), handler.getObjectFromString("2")) == 0;
    }
}