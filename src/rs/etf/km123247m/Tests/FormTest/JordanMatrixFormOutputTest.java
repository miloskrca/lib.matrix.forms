package rs.etf.km123247m.Tests.FormTest;

import org.ejml.data.Complex64F;
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
import rs.etf.km123247m.PropertyManager.PropertyManager;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class JordanMatrixFormOutputTest {

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

    protected void assertAllOK(JordanMatrixForm matrixForm, String path) throws Exception {
        IMatrix matrix = matrixForm.getTransitionalMatrix();
        ArrayList<Object> roots = matrixForm.getRoots();
        String[] rootStrings = new String[roots.size()];
        for (int i = 0; i < roots.size(); i++) {
            rootStrings[i] = roots.get(i).toString();
        }
        if (path.equals(file(1))) {
            check(matrix, 0, 0, "1");
            check(matrix, 0, 1, "0");
            check(matrix, 0, 2, "0");
            check(matrix, 1, 0, "0");
            check(matrix, 1, 1, "1");
            check(matrix, 1, 2, "0");
            check(matrix, 2, 0, "0");
            check(matrix, 2, 1, "0");
            check(matrix, 2, 2, "(x-3)*(x-2)^2");
            assertArrayEquals(new String[]{"3", "2", "2"}, rootStrings);
        }
        if (path.equals(file(2))) {
            check(matrix, 0, 0, "1");
            check(matrix, 0, 1, "0");
            check(matrix, 0, 2, "0");
            check(matrix, 1, 0, "0");
            check(matrix, 1, 1, "1");
            check(matrix, 1, 2, "0");
            check(matrix, 2, 0, "0");
            check(matrix, 2, 1, "0");
            check(matrix, 2, 2, "x*(x-4)*(x-3)");
            assertArrayEquals(new String[]{"4", "3"}, rootStrings);
        }
        if (path.equals(file(3))) {
            if (PropertyManager.getProperty("convert_to_rational").equals("0")) {
                check(matrix, 0, 0, "1");
                check(matrix, 0, 1, "0");
                check(matrix, 1, 0, "0");
                check(matrix, 1, 1, "(x+0.37228132326901475)*(x-5.372281323269016)");
                assertArrayEquals(new String[]{
                        (new Complex64F(-0.37228132326901475, 0.0)).toString(),
                        (new Complex64F(5.372281323269016, 0.0)).toString()
                }, rootStrings);
            } else {
                check(matrix, 0, 0, "1");
                check(matrix, 0, 1, "0");
                check(matrix, 1, 0, "0");
                check(matrix, 1, 1, "(x+137/368)*(x-736/137)");
                assertArrayEquals(new String[]{
                        "-137/368",
                        "736/137"
                }, rootStrings);

            }
        }
        if (path.equals(file(4))) {
            if (PropertyManager.getProperty("convert_to_rational").equals("0")) {
                check(matrix, 0, 0, "1");
                check(matrix, 0, 1, "0");
                check(matrix, 1, 0, "0");
                check(matrix, 1, 1, "(x-(2.5+1.936491673103709*i))*(x-(2.5-1.936491673103709*i))");
                assertArrayEquals(new String[]{
                        (new Complex64F(2.5, -1.936491673103709)).toString(),
                        (new Complex64F(2.5, 1.936491673103709)).toString()
                }, rootStrings);
            } else {
                check(matrix, 0, 0, "1");
                check(matrix, 0, 1, "0");
                check(matrix, 1, 0, "0");
                check(matrix, 1, 1, "(x-(5/2+122/63*i))*(x-(5/2-122/63*i))");
                assertArrayEquals(new String[]{
                        "5/2+122/63*i",
                        "5/2-122/63*i"
                }, rootStrings);

            }
        }
    }

    protected void check(IMatrix matrix, int row, int column, String value) throws Exception {
        assertEquals(value, matrix.get(row, column).getElement().toString());
    }
}