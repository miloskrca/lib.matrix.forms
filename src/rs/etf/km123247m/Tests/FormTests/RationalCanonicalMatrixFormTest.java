package rs.etf.km123247m.Tests.FormTests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.PolynomialRationalCanonicalMatrixForm;
import rs.etf.km123247m.Matrix.Forms.Implementation.RationalCanonicalMatrixForm;
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

public class RationalCanonicalMatrixFormTest {

    private String[] paths;
    private String lastOutput = "";
    private String output = "";

    @Before
    public void setUp() throws Exception {
        paths = new String[] {
            "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix1.txt",
            "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix2.txt",
            "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix3.txt",
            "./TestData/FormTests/RationalCanonical/RationalCanonicalMatrixFormTestMatrix4.txt"
        };
    }

    @Test
    public void testProcess() throws Exception {

        for (String path : paths) {
            File file = new File(path);
            IParser parser = new IExprMatrixFileParser(file);
            IMatrix matrix = (ArrayMatrix) parser.parseInput();

            MatrixHandler handler = new SymJaMatrixHandler(matrix);
            MatrixForm matrixForm = new PolynomialRationalCanonicalMatrixForm(handler);

            Observer observer = new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    FormEvent event = (FormEvent)arg;
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
}