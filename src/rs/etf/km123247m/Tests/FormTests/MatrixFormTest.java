package rs.etf.km123247m.Tests.FormTests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Matrix.Forms.Implementation.OriginalSmithMatrixForm;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Observers.Event.FormEvent;
import rs.etf.km123247m.Observers.FormObserver;

import java.util.Observable;

public class MatrixFormTest {

    private MatrixForm form;
    private MatrixHandler handler;
    private boolean started;
    private boolean processed;

    @Before
    public void setUp() throws Exception {
        IMatrix matrix = new ArrayMatrix(1, 1);
        handler = new SymJaMatrixHandler(matrix);
        form = new OriginalSmithMatrixForm(handler);
        started = false;
        processed = false;
    }

    @Test
    public void testStart() throws Exception {
        FormObserver observer = new FormObserver() {
            @Override
            public void update(Observable o, Object arg) {
                FormEvent event = (FormEvent)arg;
                MatrixForm oForm = ((MatrixForm)o);
                assert oForm == form;
                assert oForm.getHandler() == handler;

                if(!started && !processed) {
                    started = true;
                    assert event.getType() == FormEvent.PROCESSING_START;
                } else if(started && !processed) {
                    processed = true;
                    assert event.getType() == FormEvent.PROCESSING_STATUS;
                } else if(started /* && processed*/) {
                    assert event.getType() == FormEvent.PROCESSING_END;
                }
            }
        };
        form.addObserver(observer);
        form.start();
    }
}