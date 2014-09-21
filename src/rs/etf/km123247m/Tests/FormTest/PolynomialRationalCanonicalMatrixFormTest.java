package rs.etf.km123247m.Tests.FormTest;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.PolynomialRationalCanonicalMatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Parser.MatrixParser.SymJa.IExprMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;

public class PolynomialRationalCanonicalMatrixFormTest {

    private String[] paths;

    @Before
    public void setUp() throws Exception {
        paths = new String[] {
            "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix1.txt",
            "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix2.txt",
            "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix3.txt",
            "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix4.txt",
            "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix5.txt",
        };
    }

    @Test
    public void testGenerateMatrixInRationalCanonicalForm() throws Exception {
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            File file = new File(path);
            IParser parser = new IExprMatrixFileParser(file);
            IMatrix matrix = (ArrayMatrix) parser.parseInput();

            MatrixHandler handler = new SymJaMatrixHandler(matrix);
            LocalPolynomialRationalCanonicalMatrixForm matrixForm = new LocalPolynomialRationalCanonicalMatrixForm(handler);
            handler.setMatrix(matrix);
//            System.out.println(matrix.toString());
            matrixForm.generateMatrixInRationalCanonicalForm();
            IMatrix finalMatrix = matrixForm.getFinalMatrix();
//            System.out.println(finalMatrix);
            switch (i) {
                case 0:
//                    "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix1.txt",
//                    | 1  0  0 |
//                    | 0  Plus[-2, x]  0 |
//                    | 0  0  Plus[4, Times[-3, x], Power[x, 2]] |
                    assert handler.compare(finalMatrix.get(0, 0).getElement(), handler.getObjectFromString("2")) == 0;
                    assert handler.compare(finalMatrix.get(0, 1).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(0, 2).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 1).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 2).getElement(), handler.getObjectFromString("-4")) == 0;
                    assert handler.compare(finalMatrix.get(2, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(2, 1).getElement(), handler.getObjectFromString("1")) == 0;
                    assert handler.compare(finalMatrix.get(2, 2).getElement(), handler.getObjectFromString("3")) == 0;
                    break;
                case 1:
//                    "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix2.txt",
//                    | Plus[1, x]  0  0 |
//                    | 0  Plus[1, x]  0 |
//                    | 0  0  Plus[1, x] |
                    assert handler.compare(finalMatrix.get(0, 0).getElement(), handler.getObjectFromString("-1")) == 0;
                    assert handler.compare(finalMatrix.get(0, 1).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(0, 2).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 1).getElement(), handler.getObjectFromString("-1")) == 0;
                    assert handler.compare(finalMatrix.get(1, 2).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(2, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(2, 1).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(2, 2).getElement(), handler.getObjectFromString("-1")) == 0;
                    break;
                case 2:
//                    "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix3.txt",
//                    | 1  0  0 |
//                    | 0  1  0 |
//                    | 0  0  Plus[4, Times[-3, x], Times[-5, Power[x, 2]], Power[x, 3]] |
                    assert handler.compare(finalMatrix.get(0, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(0, 1).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(0, 2).getElement(), handler.getObjectFromString("-4")) == 0;
                    assert handler.compare(finalMatrix.get(1, 0).getElement(), handler.getObjectFromString("1")) == 0;
                    assert handler.compare(finalMatrix.get(1, 1).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 2).getElement(), handler.getObjectFromString("3")) == 0;
                    assert handler.compare(finalMatrix.get(2, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(2, 1).getElement(), handler.getObjectFromString("1")) == 0;
                    assert handler.compare(finalMatrix.get(2, 2).getElement(), handler.getObjectFromString("5")) == 0;
                    break;
                case 3:
//                    "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix4.txt",
//                    | 1  0 |
//                    | 0  Plus[3, Times[2, x], Power[x, 2]] |
                    assert handler.compare(finalMatrix.get(0, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(0, 1).getElement(), handler.getObjectFromString("-3")) == 0;
                    assert handler.compare(finalMatrix.get(1, 0).getElement(), handler.getObjectFromString("1")) == 0;
                    assert handler.compare(finalMatrix.get(1, 1).getElement(), handler.getObjectFromString("-2")) == 0;
                    break;
                case 4:
//                    "./TestData/FormTest/RationalCanonical/Polynomial/PolynomialRationalCanonicalMatrixFormTestMatrix4.txt",
//                    | Plus[-2, x]  0 |
//                    | 0  Plus[-2, x] |
                    assert handler.compare(finalMatrix.get(0, 0).getElement(), handler.getObjectFromString("2")) == 0;
                    assert handler.compare(finalMatrix.get(0, 1).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 0).getElement(), handler.getObjectFromString("0")) == 0;
                    assert handler.compare(finalMatrix.get(1, 1).getElement(), handler.getObjectFromString("2")) == 0;
                    break;
            }
        }
    }

    private class LocalPolynomialRationalCanonicalMatrixForm extends PolynomialRationalCanonicalMatrixForm {

        public LocalPolynomialRationalCanonicalMatrixForm(MatrixHandler handler) throws Exception {
            super(handler);
        }

        public void generateMatrixInRationalCanonicalForm() throws Exception {
            super.generateMatrixInRationalCanonicalForm();
        }

    }
}