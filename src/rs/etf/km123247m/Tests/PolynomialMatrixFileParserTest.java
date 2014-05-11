package rs.etf.km123247m.Tests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.ArrayMatrix;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Parser.MatrixParser.PolynomialMatrixParser.PolynomialMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;
import rs.etf.km123247m.Polynomial.Polynomial;
import rs.etf.km123247m.Polynomial.Term;

import java.io.File;

public class PolynomialMatrixFileParserTest {

    IParser parser;
    IMatrix matrix;

    @Before
    public void setUp() throws Exception {

        matrix = null;
        String path = "./TestData/matrix.txt";
        File file = new File(path);
        parser = new PolynomialMatrixFileParser(file);
        matrix = (ArrayMatrix)parser.parseInput();
    }

    @Test
    public void testParseMatrix() throws Exception {
        assert matrix != null;
        Polynomial poly00 = ((Polynomial)matrix.get(0, 0));
        Polynomial poly01 = ((Polynomial)matrix.get(0, 1));
        Polynomial poly10 = ((Polynomial)matrix.get(1, 0));
        Polynomial poly11 = ((Polynomial)matrix.get(1, 1));
        Term term000 = poly00.getTerm(0);
        Term term100 = poly10.getTerm(0);
        Term term101 = poly10.getTerm(1);

        //test polynomials
        assert poly00.toString().equals("x^2");
        assert poly01.toString().equals("x^3");
        assert poly10.toString().equals("3*x+3");
        assert poly11.toString().equals("1+x");

        //test terms
        assert term000.toString().equals("x^2");
        assert term100.toString().equals("3*x");
        assert term101.toString().equals("3");
    }
}