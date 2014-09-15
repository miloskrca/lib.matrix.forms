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

        File file = new File("./TestData/HandlerTest/MathITMatrixHandlerTest2.txt");
        IParser parser = new MathITMatrixFileParser(file);
        IMatrix matrix = (ArrayMatrix) parser.parseInput();
        handler = new MathITMatrixHandler(matrix);

        IMatrix startMatrix = new ArrayMatrix(2, 2);
        startMatrix.set(new MatrixCell(0, 0, handler.getObjectFromString("1")));
        startMatrix.set(new MatrixCell(0, 1, handler.getObjectFromString("2")));
        startMatrix.set(new MatrixCell(1, 0, handler.getObjectFromString("3")));
        startMatrix.set(new MatrixCell(1, 1, handler.getObjectFromString("4")));
        calcT(startMatrix, handler.getMatrix());
    }


    protected IMatrix calcT(IMatrix startMatrix, IMatrix matrix) throws Exception {
        int rows = startMatrix.getRowNumber();
        int columns = startMatrix.getColumnNumber();
        IMatrix t = null;

        HashMap<Integer, IMatrix> pMatrices = new HashMap<Integer, IMatrix>();
        int highestPower = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Object item = matrix.get(i, j).getElement();
                int deg = handler.getHighestPower(item);
                if(deg > highestPower) {
                    highestPower = deg;
                }
                while(deg >= 0) {
                    if(handler.hasElementWithPower(item, deg)) {
                        double coefficient = Double.parseDouble(handler.getCoefficientForPower(item, deg).toString());
                        initMatrix(pMatrices, deg, rows);
                        pMatrices.get(deg).set(new MatrixCell(i, j, handler.getObjectFromString(String.valueOf(coefficient))));
                    }
                    deg--;
                }
            }
        }

        for (int power = 0; power <= highestPower; power++) {
            if(pMatrices.containsKey(power)) {
                if(t == null) {
                    t = new ArrayMatrix(rows, columns);
                    t.initWith(handler.getObjectFromString("0"));
                    if(power == 0) {
                        t = pMatrices.get(power);
                    } else {
                        handler.multiply(pMatrices.get(power), handler.power(startMatrix, power), t);
                    }
                } else {
                    IMatrix tempResult = new ArrayMatrix(matrix.getRowNumber(), matrix.getColumnNumber());
                    tempResult.initWith(handler.getZero());
                    if(power == 0) {
                        tempResult = pMatrices.get(power);
                    } else {
                        // tempResult =  A^n*P(n)
                        handler.multiply(handler.power(startMatrix, power), pMatrices.get(power), tempResult);
                    }
                    IMatrix tempResult2 = new ArrayMatrix(matrix.getRowNumber(), matrix.getColumnNumber());
                    tempResult2.initWith(handler.getZero());
                    // tempResult2 = t + A^n*P(n)
                    handler.add(t, tempResult, tempResult2);
                    t = tempResult2;
                }
            }
        }

        return t;
    }

    protected void initMatrix(HashMap<Integer, IMatrix> pMatrices, int power, int range) throws Exception {
        if(!pMatrices.containsKey(power)) {
            IMatrix matrix = new ArrayMatrix(range, range);
            matrix.initWith(handler.getZero());
            pMatrices.put(power, matrix);
        }
    }

}
