package rs.etf.km123247m.Tests.FormTests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.SmithMatrixForm;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.MathITMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Parser.MatrixParser.MathIT.MathITMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class SmithMatrixFormOutputTest {

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

                file(6),
                file(7),
                file(8),
                file(9),

        };
    }

    protected String file(int number) {
        return "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix" + number + ".txt";
    }

    @Test
    public void testProcess() throws Exception {

        for (final String path : paths) {
            File file = new File(path);
            IParser parser = new MathITMatrixFileParser(file);
            IMatrix matrix = (ArrayMatrix) parser.parseInput();

            MatrixHandler handler = new MathITMatrixHandler(matrix);
            MatrixForm matrixForm = new SmithMatrixForm(handler);

            Observer observer = new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    FormEvent event = (FormEvent) arg;
                    MatrixForm form = (MatrixForm) o;
                    switch (event.getType()) {
                        case FormEvent.PROCESSING_START:
                            output = "PROCESSING_START\n";
                            output += form.getHandler().getMatrix().toString();
                            break;
                        case FormEvent.PROCESSING_STEP:
                            output = "PROCESSING_STEP " + event.getMessage() + "\n";
                            output += form.getHandler().getMatrix().toString();
                            break;
                        case FormEvent.PROCESSING_END:
                            output = "PROCESSING_END\n";
                            output += form.getHandler().getMatrix().toString();
                            output += "PROCESSING_END\n";
                            try {
                                assertAllOK(form, path);
                            } catch (Exception e) {
                                System.out.println("Exception: " + e.getMessage());
                            }
                            break;
                        case FormEvent.PROCESSING_INFO:
                            output = "PROCESSING_INFO " + event.getMessage() + "\n";
                            break;
                        case FormEvent.PROCESSING_EXCEPTION:
                            System.out.println("Exception: " + event.getMessage());
                            output = "PROCESSING_EXCEPTION";
                            break;
                    }
                    assert !lastOutput.equals(output);
                    lastOutput = output;
                    System.out.println(output);
                }
            };
            matrixForm.addObserver(observer);
//            matrixForm.start();
        }
    }

    protected void assertAllOK(MatrixForm matrixForm, String path) throws Exception {
        if (path.equals(file(1))) {
            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "x");

        } else if (path.equals(file(2))) {
            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "x^2-8*x-9");

        } else if (path.equals(file(3))) {
            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0"); check(matrixForm, 0, 2, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "1"); check(matrixForm, 1, 2, "0");
            check(matrixForm, 2, 0, "0"); check(matrixForm, 2, 1, "0"); check(matrixForm, 2, 2, "x^3+0.5*x^2+x-1");

        } else if (path.equals(file(4))) {
            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "x^4-x^2+x");

        } else if (path.equals(file(6))) {
            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0"); check(matrixForm, 0, 2, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "1"); check(matrixForm, 1, 2, "0");
            check(matrixForm, 2, 0, "0"); check(matrixForm, 2, 1, "0"); check(matrixForm, 2, 2, "x^3-9*x^2+26*x-24");

        } else if (path.equals(file(7))) {
            check(matrixForm, 0, 0, "x-2"); check(matrixForm, 0, 1, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "x-2");

        } else if (path.equals(file(8))) {
            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0"); check(matrixForm, 0, 2, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "1"); check(matrixForm, 1, 2, "0");
            check(matrixForm, 2, 0, "0"); check(matrixForm, 2, 1, "0"); check(matrixForm, 2, 2, "x^3+x^2-3*x-4");

        } else if (path.equals(file(9))) {
            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0"); check(matrixForm, 0, 2, "0");
            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "1"); check(matrixForm, 1, 2, "0");
            check(matrixForm, 2, 0, "0"); check(matrixForm, 2, 1, "0"); check(matrixForm, 2, 2, "x^3-7*x^2+16*x-12");

        }
    }

    protected void check(MatrixForm matrixForm, int row, int column, String value) throws Exception {
        assert matrixForm.getHandler().compare(
                matrixForm.getHandler().getMatrix().get(row, column).getElement(),
                matrixForm.getHandler().getObjectFromString(value)
        ) == 0;
    }
}