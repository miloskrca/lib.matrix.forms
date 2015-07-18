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

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class JordanMatrixFormTest {

    private String[] paths;
    private String lastOutput = "";
    private String output = "";

    @Before
    public void setUp() throws Exception {
        paths = new String[]{
                file(1),
                file(2),
                file(3),
                file(4),
                file(5),
                file(6),
                file(7),
                file(8),
                file(9),
        };
    }

    protected String file(int number) {
        return "./TestData/FormTest/Jordan/JordanMatrixFormTestMatrix" + number + ".txt";
    }

    @Test
    public void testProcess() throws Exception {

        for (final String path : paths) {
            lastOutput = "";
            output = "";
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
                            assertExceptionOk(event, path);
                            output = "PROCESSING_EXCEPTION";
                            break;
                    }
                    assertNotEquals(lastOutput, output);
                    lastOutput = output;
//                    System.out.println(output);
                }
            };
            matrixForm.addObserver(observer);
            matrixForm.start();
        }
    }

    private void assertExceptionOk(FormEvent event, String path) {
        if (path.equals(file(7))) {
            assertEquals("Exception not recognized", FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL, event.getMessage());
        } else if (path.equals(file(8))) {
            assertEquals("Exception not recognized", FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL, event.getMessage());
        } else if (path.equals(file(9))) {
            assertEquals("Exception not recognized", FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL, event.getMessage());
        } else {
            fail("Not expected exception: " + event.getMessage() + " file: " + path);
        }
    }

    protected void assertAllOK(JordanMatrixForm matrixForm, String path) throws Exception {
        IMatrix matrix = matrixForm.getTransitionalMatrix();
        ArrayList<ArrayList<Object>> roots = matrixForm.getRoots();
        ArrayList<String> rootStringsArrayList = new ArrayList<String>();
        for (ArrayList<Object> rootList : roots) {
            for (Object rootListItem : rootList) {
                rootStringsArrayList.add(rootListItem.toString());
            }
        }
        String[] rootStrings = rootStringsArrayList.toArray(new String[rootStringsArrayList.size()]);
        if (path.equals(file(1))) {
            check(matrix, 0, 0, "1");
            check(matrix, 0, 1, "0");
            check(matrix, 0, 2, "0");
            check(matrix, 1, 0, "0");
            check(matrix, 1, 1, "1");
            check(matrix, 1, 2, "0");
            check(matrix, 2, 0, "0");
            check(matrix, 2, 1, "0");
            check(matrix, 2, 2, "(x-3)*((-x+2)^2)");
            assertArrayEquals(new String[]{"2", "2", "3"}, rootStrings);
        } else if (path.equals(file(2))) {
            check(matrix, 0, 0, "1");
            check(matrix, 0, 1, "0");
            check(matrix, 0, 2, "0");
            check(matrix, 1, 0, "0");
            check(matrix, 1, 1, "1");
            check(matrix, 1, 2, "0");
            check(matrix, 2, 0, "0");
            check(matrix, 2, 1, "0");
            check(matrix, 2, 2, "x*(x-4)*(x-3)");
            assertArrayEquals(new String[]{"0", "3", "4"}, rootStrings);
        } else if (path.equals(file(3))) {
            check(matrix, 0, 0, "1");
            check(matrix, 0, 1, "0");
            check(matrix, 1, 0, "0");
            check(matrix, 1, 1, "(x+1/2*33^(1/2)-5/2)*(x-1/2*33^(1/2)-5/2)");
            assertArrayEquals(new String[]{
                    "-1/2*33^(1/2)+5/2",
                    "1/2*33^(1/2)+5/2"
            }, rootStrings);
        } else if (path.equals(file(4))) {
            check(matrix, 0, 0, "1");
            check(matrix, 0, 1, "0");
            check(matrix, 1, 0, "0");
            check(matrix, 1, 1, "(x+I*1/2*15^(1/2)-5/2)*(x-I*1/2*15^(1/2)-5/2)");
            assertArrayEquals(new String[]{
                    "-I*1/2*15^(1/2)+5/2",
                    "I*1/2*15^(1/2)+5/2"
            }, rootStrings);
        } else if (path.equals(file(5))) {
            check(matrix, 0, 0, "1");
            check(matrix, 0, 1, "0");
            check(matrix, 0, 2, "0");
            check(matrix, 0, 3, "0");
            check(matrix, 1, 0, "0");
            check(matrix, 1, 1, "1");
            check(matrix, 1, 2, "0");
            check(matrix, 1, 3, "0");
            check(matrix, 2, 0, "0");
            check(matrix, 2, 1, "0");
            check(matrix, 2, 2, "1");
            check(matrix, 2, 3, "0");
            check(matrix, 3, 0, "0");
            check(matrix, 3, 1, "0");
            check(matrix, 3, 2, "0");
            check(matrix, 3, 3, "(x-7)*(x-2)*((-x+5)^2)");
            assertEquals("Matrix output string wrong!",
                    "| 2  0  0  0 |" +
                            "| 0  5  1  0 |" +
                            "| 0  0  5  0 |" +
                            "| 0  0  0  7 |",
                    matrixForm.getFinalMatrix().toString().replace("\n", ""));
            assertArrayEquals(new String[]{"2", "5", "5", "7"}, rootStrings);
        } else if (path.equals(file(6))) {
            assertEquals("Matrix output string wrong!",
                    "| 1  0  0 |" +
                            "| 0  (x-2)  0 |" +
                            "| 0  0  (x-3)*(x-2) |",
                    matrix.toString().replace("\n", ""));
            assertEquals("Matrix output string wrong!",
                    "| 2  0  0 |" +
                            "| 0  2  0 |" +
                            "| 0  0  3 |",
                    matrixForm.getFinalMatrix().toString().replace("\n", ""));
            assertArrayEquals(new String[]{"2", "2", "3"}, rootStrings);
        } else if (path.equals(file(7))) {
            fail("Exception '" + FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL + "' expected.");
        } else if (path.equals(file(8))) {
            fail("Exception '" + FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL + "' expected.");
        } else if (path.equals(file(9))) {
            fail("Exception '" + FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL + "' expected.");
        }
    }

    protected void check(IMatrix matrix, int row, int column, String value) throws Exception {
        assertEquals("Element not correct!", value, matrix.get(row, column).getElement().toString());
    }
}