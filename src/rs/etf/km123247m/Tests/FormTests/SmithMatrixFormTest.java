package rs.etf.km123247m.Tests.FormTests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.SmithMatrixForm;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Observers.Event.FormEvent;
import rs.etf.km123247m.Parser.MatrixParser.SymJa.IExprMatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.IParser;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class SmithMatrixFormTest {

    private MatrixForm form;
    private String lastOutput = "";
    private String output = "";

    @Before
    public void setUp() throws Exception {
        String path = "./TestData/FormTests/SmithMatrixFormTestMatrix3.txt";
        File file = new File(path);
        IParser parser = new IExprMatrixFileParser(file);
        IMatrix matrix = (ArrayMatrix) parser.parseInput();

        MatrixHandler handler = new SymJaMatrixHandler(matrix);
        form = new SmithMatrixForm(handler);
    }

    @Test
    public void testProcess() throws Exception {

        Observer observer = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                FormEvent event = (FormEvent)arg;
                switch (event.getType()) {
                    case FormEvent.PROCESSING_START:
                        System.out.println("Start");
                        output = "PROCESSING_START";
                        break;
                    case FormEvent.PROCESSING_STATUS:
                        System.out.println("Status");
                        output = ((MatrixHandler)event.getObject()).getMatrix().toString();
                        break;
                    case FormEvent.PROCESSING_END:
                        System.out.println("End");
                        output = "PROCESSING_END";
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
        form.addObserver(observer);
        form.start();
    }
}