package rs.etf.km123247m.Tests.FormTests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.SmithMatrixForm;
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

public class SmithMatrixFormOutputTest {

    private String[] paths;
    private String lastOutput = "";
    private String output = "";

    @Before
    public void setUp() throws Exception {
        paths = new String[] {
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix1.txt",
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix2.txt",
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix3.txt",
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix4.txt",
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix5.txt",
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix6.txt",
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix7.txt",
            "./TestData/FormTests/Smith/SmithMatrixFormTestMatrix8.txt"
        };
    }

    @Test
    public void testProcess() throws Exception {

        for (String path : paths) {
            File file = new File(path);
            IParser parser = new IExprMatrixFileParser(file);
            IMatrix matrix = (ArrayMatrix) parser.parseInput();

            MatrixHandler handler = new SymJaMatrixHandler(matrix);
            MatrixForm matrixForm = new SmithMatrixForm(handler);

            Observer observer = new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    FormEvent event = (FormEvent)arg;
                    MatrixForm form = (MatrixForm) o;
                    switch (event.getType()) {
                        case FormEvent.PROCESSING_START:
                            output = "PROCESSING_START\n";
                            output += form.getHandler().getMatrix().toString();
                            break;
                        case FormEvent.PROCESSING_STATUS:
                            output = "PROCESSING_STATUS " + event.getMessage() + "\n";
                            output += form.getHandler().getMatrix().toString();
                            break;
                        case FormEvent.PROCESSING_END:
                            output = "PROCESSING_END\n";
                            output += form.getHandler().getMatrix().toString();
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