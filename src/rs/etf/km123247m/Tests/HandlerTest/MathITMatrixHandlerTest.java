//package rs.etf.km123247m.Tests.HandlerTest;
//
//import org.junit.Before;
//import org.junit.Test;
//import rs.etf.km123247m.Matrix.Handler.Implementation.MathITMatrixHandler;
//import rs.etf.km123247m.Matrix.IMatrix;
//import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
//import rs.etf.km123247m.Parser.MatrixParser.MathIT.MathITMatrixFileParser;
//import rs.etf.km123247m.Parser.ParserTypes.IParser;
//
//import java.io.File;
//
//public class MathITMatrixHandlerTest {
//
//    private MathITMatrixHandler handler;
//
//    @Before
//    public void setUp() throws Exception {
//        File file = new File("./TestData/HandlerTest/MathITMatrixHandlerTest3.txt");
//        IParser parser = new MathITMatrixFileParser(file);
//        IMatrix matrix = (ArrayMatrix) parser.parseInput();
//        handler = new MathITMatrixHandler(matrix);
//    }
//
//    @Test
//    public void testSomething() throws Exception {
//        IMatrix inverted = handler.invertMatrix(handler.getMatrix());
//        IMatrix result = new ArrayMatrix(2, 2);
//        result.initWith(handler.getZero());
//        handler.multiply(inverted, handler.getMatrix(), result);
////        System.out.println(handler.getMatrix().toString());
////        System.out.println(inverted.toString());
////        System.out.println(result.toString());
//    }
//}