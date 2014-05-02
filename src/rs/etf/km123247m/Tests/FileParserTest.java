package rs.etf.km123247m.Tests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Matrix;
import rs.etf.km123247m.Parser.FileParser;
import rs.etf.km123247m.Polinomial.Polynomial;

public class FileParserTest {

    FileParser parser;
    Matrix<Polynomial> matrix;

    @Before
    public void setUp() throws Exception {

        // @TODO: Get the pah right
        String path = FileParserTest.class
                .getClassLoader()
                .getResource("matrix.txt")
                .getPath()
                .replaceAll("%20", " ");
        parser = new FileParser(path);
        matrix = parser.parseMatrix();
    }

    @Test
    public void testParseMatrix() throws Exception {
        assert matrix.get(0, 0).getTerm(0).toString().equals("x^2");
    }
}