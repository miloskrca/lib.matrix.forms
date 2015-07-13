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
 * SymJaMatrixHandlerTest.txt
 * | 2+x,                x^2-x;       |
 * | -3+8*x^2-x^8+7*x^4, 5*x^4-x^2+x; |
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
        assert handler.isZeroElement(pair.getCoefficient());
        assert handler.compare(pair.getPower(), handler.getObjectFromString("2")) == 0;
    }

    @Test
    public void testHasElementWithPower() throws Exception {
        // Test for powers that are in polynomial

        // 2+x
        assert handler.hasElementWithPower(handler.getMatrix().get(0, 0).getElement(), 0);
        // x^2-x
        assert handler.hasElementWithPower(handler.getMatrix().get(0, 1).getElement(), 2);
        // -3+8*x^2-x^8+7*x^4
        assert handler.hasElementWithPower(handler.getMatrix().get(1, 0).getElement(), 8);
        // 5*x^4-x^2+x
        assert handler.hasElementWithPower(handler.getMatrix().get(1, 1).getElement(), 4);

        // Test for powers that are not in polynomial

        // 2+x
        assert !handler.hasElementWithPower(handler.getMatrix().get(0, 0).getElement(), 2);
        // x^2-x
        assert !handler.hasElementWithPower(handler.getMatrix().get(0, 1).getElement(), 0);
        // -3+8*x^2-x^8+7*x^4
        assert !handler.hasElementWithPower(handler.getMatrix().get(1, 0).getElement(), 1);
        // 5*x^4-x^2+x
        assert !handler.hasElementWithPower(handler.getMatrix().get(1, 1).getElement(), 3);
    }

    @Test
    public void testGetCoefficientForPower() throws Exception {
        // 2+x
        assert "2".equals(handler.getCoefficientForPower(handler.getMatrix().get(0, 0).getElement(), 0).toString());
        // x^2-x
        assert "1".equals(handler.getCoefficientForPower(handler.getMatrix().get(0, 1).getElement(), 2).toString());
        // -3+8*x^2-x^8+7*x
        assert "-1".equals(handler.getCoefficientForPower(handler.getMatrix().get(1, 0).getElement(), 8).toString());
        // -3+8*x^2-x^8+7*x^4
        assert "8".equals(handler.getCoefficientForPower(handler.getMatrix().get(1, 0).getElement(), 2).toString());
        // 5*x^4-x^2+x
        assert "5".equals(handler.getCoefficientForPower(handler.getMatrix().get(1, 1).getElement(), 4).toString());

        assert "1/2".equals(handler.getCoefficientForPower(handler.getObjectFromString("1/2*x^2"), 2).toString());
        assert "0".equals(handler.getCoefficientForPower(handler.getObjectFromString("x^2 + 3"), 1).toString());

    }
}