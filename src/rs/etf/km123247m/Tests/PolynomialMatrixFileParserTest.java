package rs.etf.km123247m.Tests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Matrix;
import rs.etf.km123247m.Parser.ParserTypes.FileParser;
import rs.etf.km123247m.Parser.MatrixParser.PolynomialMatrixParser.PolynomialMatrixFileParser;
import rs.etf.km123247m.Polynomial.Polynomial;

import java.io.File;
import java.net.URL;

public class PolynomialMatrixFileParserTest {

    FileParser parser;
    Matrix matrix;

    @Before
    public void setUp() throws Exception {

        matrix = null;
        URL res = PolynomialMatrixFileParserTest.class
                .getResource("TestData/matrix.txt");
        if(res != null) {
            String path = res.getPath()
                    .replaceAll("%20", " ");
            File file = new File(path);
            parser = new PolynomialMatrixFileParser(file);
            matrix = (Matrix)parser.parseInput();
        }
    }

    @Test
    public void testParseMatrix() throws Exception {
        assert matrix != null;
        assert ((Polynomial)matrix.get(0, 0)).getTerm(0).toString().equals("x^2");
    }
}