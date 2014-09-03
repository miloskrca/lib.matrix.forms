package rs.etf.km123247m.Tests.FormTests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.PolynomialRationalCanonicalMatrixForm;
import rs.etf.km123247m.Matrix.Forms.Implementation.RationalCanonicalMatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.ApacheMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Observer.FormObserver;
import rs.etf.km123247m.Parser.MatrixParser.Apache.ApacheMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;
import java.util.Observable;

public class RationalCanonicalMatrixFormTest {

    private String[] paths;
    private String lastOutput = "";
    private String output = "";

    @Before
    public void setUp() throws Exception {
        paths = new String[] {
            file(1),
            file(2),
            file(3),
            file(4),
        };
    }

    protected String file(int number) {
        return "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix" + number + ".txt";
    }

    @Test
    public void testProcess() throws Exception {

        for (int i = 0; i < paths.length; i++) {
            final String path = paths[i];
            File file = new File(path);
            IParser parser = new ApacheMatrixFileParser(file);
            IMatrix matrix = (ArrayMatrix) parser.parseInput();

            MatrixHandler handler = new ApacheMatrixHandler(matrix);
            final PolynomialRationalCanonicalMatrixForm matrixForm = new PolynomialRationalCanonicalMatrixForm(handler);

            final int finalI = i;
            FormObserver observer = new FormObserver() {
                @Override
                public void update(Observable o, Object arg) {
                    FormEvent event = (FormEvent) arg;
                    RationalCanonicalMatrixForm form = (RationalCanonicalMatrixForm) o;
                    switch (event.getType()) {
                        case FormEvent.PROCESSING_START:
                            output = "PROCESSING_START\n";
                            output += form.getStartMatrix().toString() + "\n";
                            output += form.getTransitionalMatrix().toString() + "\n";
                            break;
                        case FormEvent.PROCESSING_STATUS:
                            output = "PROCESSING_STATUS " + event.getMessage() + "\n";
                            output += form.getTransitionalMatrix().toString() + "\n";
                            break;
                        case FormEvent.PROCESSING_END:
                            output = "PROCESSING_END\n";
                            output += form.getTransitionalMatrix().toString() + "\n";
                            output += form.getFinalMatrix().toString() + "\n";
                            output += "PROCESSING_END\n";
                            try {
                                assertAllOK(matrixForm, finalI);
                            } catch (Exception e) {
                                System.out.println("Exception: " + e.getMessage());
                            }
                            break;
                        case FormEvent.PROCESSING_EXCEPTION:
                            System.out.println("Exception: " + event.getMessage());
                            output = "PROCESSING_EXCEPTION";
                            break;
                    }
                    // @TODO: 4-th test case is not passing, remainder of the division is in the way
                    // assert no endless loop
                    assert !lastOutput.equals(output);
                    lastOutput = output;
                    System.out.println(output);
                }
            };
            matrixForm.addObserver(observer);
            matrixForm.start();
        }
    }

    protected void assertAllOK(PolynomialRationalCanonicalMatrixForm matrixForm, int path) throws Exception {
        IMatrix matrix = matrixForm.getFinalMatrix();
        MatrixHandler handler = matrixForm.getHandler();
        switch (path) {
            case 0: // "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix1.txt",
//                | 1  0 |
//                | 0  Plus[-2, Times[-5, x], Power[x, 2]] |
                assert handler.compare(matrix.get(0, 0).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(0, 1).getElement(), handler.getObjectFromString("2")) == 0;
                assert handler.compare(matrix.get(1, 0).getElement(), handler.getObjectFromString("1")) == 0;
                assert handler.compare(matrix.get(1, 1).getElement(), handler.getObjectFromString("5")) == 0;
                break;
            case 1: // "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix1.txt",
//                | 1  0 |
//                | 0  Plus[-2, Times[-5, x], Power[x, 2]] |
                assert handler.compare(matrix.get(0, 0).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(0, 1).getElement(), handler.getObjectFromString("7")) == 0;
                assert handler.compare(matrix.get(1, 0).getElement(), handler.getObjectFromString("1")) == 0;
                assert handler.compare(matrix.get(1, 1).getElement(), handler.getObjectFromString("6")) == 0;
                break;
            case 2: // "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix3.txt",
//                | 1  0  0 |
//                | 0, x - 2, 0 |
//                | 0, 0, x^2 - 5*x + 6 |
                assert handler.compare(matrix.get(0, 0).getElement(), handler.getObjectFromString("2")) == 0;
                assert handler.compare(matrix.get(0, 1).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(0, 2).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(1, 0).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(1, 1).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(1, 2).getElement(), handler.getObjectFromString("-6")) == 0;
                assert handler.compare(matrix.get(2, 0).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(2, 1).getElement(), handler.getObjectFromString("1")) == 0;
                assert handler.compare(matrix.get(2, 2).getElement(), handler.getObjectFromString("5")) == 0;
                break;
            case 3: // "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix4.txt"
//                | 1  0  0 |
//                | 0  1  0 |
//                | 0  0  Plus[Times[-1, Power[x, 2]], Power[x, 3]] |
                assert handler.compare(matrix.get(0, 0).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(0, 1).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(0, 2).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(1, 0).getElement(), handler.getObjectFromString("1")) == 0;
                assert handler.compare(matrix.get(1, 1).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(1, 2).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(2, 0).getElement(), handler.getObjectFromString("0")) == 0;
                assert handler.compare(matrix.get(2, 1).getElement(), handler.getObjectFromString("1")) == 0;
                assert handler.compare(matrix.get(2, 2).getElement(), handler.getObjectFromString("1")) == 0;
                break;
        }
    }
}