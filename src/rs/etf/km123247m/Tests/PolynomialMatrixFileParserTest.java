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

    Polynomial poly00;
    Polynomial poly01;
    Polynomial poly10;
    Polynomial poly11;

    @Before
    public void setUp() throws Exception {

        matrix = null;
        String path = "./TestData/PolynomialMatrixFileParserTestMatrix.txt";
        File file = new File(path);
        parser = new PolynomialMatrixFileParser(file);
        matrix = (ArrayMatrix)parser.parseInput();
        assert matrix != null;

        poly00 = ((Polynomial)matrix.get(0, 0));
        poly01 = ((Polynomial)matrix.get(0, 1));
        poly10 = ((Polynomial)matrix.get(1, 0));
        poly11 = ((Polynomial)matrix.get(1, 1));
    }

    @Test
    public void testParseMatrix() throws Exception {
        //test polynomials
        assert poly00.toString().equals("x^2");
        assert poly01.toString().equals("x^3");
        assert poly10.toString().equals("3*x+3");
        assert poly11.toString().equals("1+x");

        //test terms
        Term term000 = poly00.getTerm(0);
        Term term100 = poly10.getTerm(0);
        Term term101 = poly10.getTerm(1);
        Term term010 = poly01.getTerm(0);

        assert term000.toString().equals("x^2");
        assert term100.toString().equals("3*x");
        assert term101.toString().equals("3");
        assert term010.toString().equals("x^3");
    }

    @Test
    public void testTermCompare() throws Exception {
        Term term000 = poly00.getTerm(0);
        Term term100 = poly10.getTerm(0);
        Term term101 = poly10.getTerm(1);
        Term term010 = poly01.getTerm(0);

        // compare terms
        assert term000.compareTo(term000) == 0;
        assert term000.compareTo(term100) == 1;
        assert term000.compareTo(term101) == 1;
        assert term000.compareTo(term010) == -1;
    }

    @Test
    public void testPolynomialCompare() throws Exception {
        // compare polynomials
        assert poly00.compareTo(poly00) == 0;
        assert poly00.compareTo(poly01) == -1;
        assert poly00.compareTo(poly10) == 1;
        assert poly00.compareTo(poly11) == 1;
    }
}