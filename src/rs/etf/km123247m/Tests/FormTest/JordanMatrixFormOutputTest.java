package rs.etf.km123247m.Tests.FormTest;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.JordanMatrixForm;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Parser.MatrixParser.SymJa.IExprMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class JordanMatrixFormOutputTest {

    private String[] paths;
    private String lastOutput = "";
    private String output = "";

    @Before
    public void setUp() throws Exception {
        paths = new String[] {
                file(1),
                file(2),
                file(3),
        };
    }

    protected String file(int number) {
        return "./TestData/FormTest/Jordan/JordanMatrixFormTestMatrix" + number + ".txt";
    }

    @Test
    public void testProcess() throws Exception {

        for (final String path : paths) {
            File file = new File(path);
            IParser parser = new IExprMatrixFileParser(file);
            IMatrix matrix = (ArrayMatrix) parser.parseInput();

            MatrixHandler handler = new SymJaMatrixHandler(matrix);
            MatrixForm matrixForm = new JordanMatrixForm(handler);

            Observer observer = new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    FormEvent event = (FormEvent) arg;
                    JordanMatrixForm form = (JordanMatrixForm) o;
                    switch (event.getType()) {
                        case FormEvent.PROCESSING_START:
                            output = "PROCESSING_START\n";
                            output += form.getStartMatrix().toString();
                            break;
                        case FormEvent.PROCESSING_STEP:
                            output = "PROCESSING_STEP " + event.getMessage() + "\n";
                            output += form.getTransitionalMatrix().toString();
                            break;
                        case FormEvent.PROCESSING_END:
                            output = "PROCESSING_END\n";
                            output += form.getFinalMatrix().toString();
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
//                    System.out.println(output);
                }
            };
            matrixForm.addObserver(observer);
            matrixForm.start();
        }
    }

    protected void assertAllOK(MatrixForm matrixForm, String path) throws Exception {
//        if (path.equals(file(1))) {
//            check(matrixForm, 0, 0, "1"); check(matrixForm, 0, 1, "0");
//            check(matrixForm, 1, 0, "0"); check(matrixForm, 1, 1, "x");
//
//        }
    }

    protected void check(MatrixForm matrixForm, int row, int column, String value) throws Exception {
        assert matrixForm.getHandler().compare(
                matrixForm.getHandler().getMatrix().get(row, column).getElement(),
                matrixForm.getHandler().getObjectFromString(value)
        ) == 0;
    }
}