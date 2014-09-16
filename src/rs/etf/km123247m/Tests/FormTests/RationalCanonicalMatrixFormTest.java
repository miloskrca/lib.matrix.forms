package rs.etf.km123247m.Tests.FormTests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.PolynomialRationalCanonicalMatrixForm;
import rs.etf.km123247m.Matrix.Forms.Implementation.RationalCanonicalMatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Observer.FormObserver;
import rs.etf.km123247m.Parser.MatrixParser.SymJa.IExprMatrixFileParser;
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
                file(5),
                file(7)
        };
    }

    protected String file(int number) {
        return "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix" + number + ".txt";
    }

    @Test
    public void testProcess() throws Exception {

        for (final String path : paths) {
            File file = new File(path);
            IParser parser = new IExprMatrixFileParser(file);
            IMatrix matrix = (ArrayMatrix) parser.parseInput();

            MatrixHandler handler = new SymJaMatrixHandler(matrix);
            final PolynomialRationalCanonicalMatrixForm matrixForm = new PolynomialRationalCanonicalMatrixForm(handler);

            FormObserver observer = new FormObserver() {
                @Override
                public void update(Observable o, Object arg) {
                    FormEvent event = (FormEvent) arg;
                    RationalCanonicalMatrixForm form = (RationalCanonicalMatrixForm) o;
                    switch (event.getType()) {
                        case FormEvent.PROCESSING_START:
                            output = "PROCESSING_START\n";
                            output += form.getStartMatrix().toString() + "\n";
                            output += form.getTransitionalMatrix(form.getRound()).toString() + "\n";
                            break;
                        case FormEvent.PROCESSING_STEP:
                            output = "PROCESSING_STEP " + event.getMessage() + "\n";
                            output += form.getTransitionalMatrix(form.getRound()).toString() + "\n";
                            output += form.getP(0).toString() + "\n";
                            break;
                        case FormEvent.PROCESSING_INFO:
                            output = "PROCESSING_INFO " + event.getMessage() + "\n";
                            output += event.getMatrix().toString() + "\n";
                            output += form.getP(0).toString() + "\n";
                            break;
                        case FormEvent.PROCESSING_END:
                            output = "PROCESSING_END\n";
                            output += form.getTransitionalMatrix(0).toString() + "\n";
                            output += form.getFinalMatrix().toString() + "\n";
                            output += form.getP(0).toString() + "\n";
                            output += form.getP(1).toString() + "\n";
                            output += "PROCESSING_END\n";
                            try {
                                assertAllOK(matrixForm, path);
                            } catch (Exception e) {
                                System.out.println("Exception: " + e.getMessage());
                            }
                            break;
                        case FormEvent.PROCESSING_EXCEPTION:
                            System.out.println("Exception: " + event.getMessage());
                            output = "PROCESSING_EXCEPTION";
                            break;
                    }
                    // assert no endless loop
                    assert !lastOutput.equals(output);
                    lastOutput = output;
//                    System.out.println(output);
                }
            };
            matrixForm.addObserver(observer);
            matrixForm.start();
        }
    }

    protected void assertAllOK(PolynomialRationalCanonicalMatrixForm matrixForm, String path) throws Exception {
        if (path.equals(file(1))) {
            check(matrixForm, 0, 0, "0"); check(matrixForm, 0, 1, "2");
            check(matrixForm, 1, 0, "1"); check(matrixForm, 1, 1, "5");

        } else if (path.equals(file(2))) {
            check(matrixForm, 0, 0, "0"); check(matrixForm, 0, 1, "7");
            check(matrixForm, 1, 0, "1"); check(matrixForm, 1, 1, "6");

        } else if (path.equals(file(3))) {
            check(matrixForm, 0, 0, "2"); check(matrixForm, 0, 1, "0"); check(matrixForm, 0, 2, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "0"); check(matrixForm, 1, 2, "-6");
            check(matrixForm, 2, 0, "0"); check(matrixForm, 2, 1, "1"); check(matrixForm, 2, 2, "5");

        } else if (path.equals(file(4))) {
            check(matrixForm, 0, 0, "0"); check(matrixForm, 0, 1, "0"); check(matrixForm, 0, 2, "12");
            check(matrixForm, 1, 0, "1"); check(matrixForm, 1, 1, "0"); check(matrixForm, 1, 2, "-16");
            check(matrixForm, 2, 0, "0"); check(matrixForm, 2, 1, "1"); check(matrixForm, 2, 2, "7");

        } else if (path.equals(file(5))) {
            check(matrixForm, 0, 0, "0"); check(matrixForm, 0, 1, "0"); check(matrixForm, 0, 2, "0");
            check(matrixForm, 1, 0, "1"); check(matrixForm, 1, 1, "0"); check(matrixForm, 1, 2, "18");
            check(matrixForm, 2, 0, "0"); check(matrixForm, 2, 1, "1"); check(matrixForm, 2, 2, "15");

        }
    }

    protected void check(PolynomialRationalCanonicalMatrixForm matrixForm, int row, int column, String value) throws Exception {
        assert matrixForm.getHandler().compare(
                matrixForm.getFinalMatrix().get(row, column).getElement(),
                matrixForm.getHandler().getObjectFromString(value)
        ) == 0;
    }
}