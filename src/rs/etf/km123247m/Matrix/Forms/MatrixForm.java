package rs.etf.km123247m.Matrix.Forms;

import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Observers.Event.FormEvent;
import rs.etf.km123247m.Observers.FormSubject;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Forms
 */
// TODO: Implement basic MatrixForm interface (lean on Smiths form implementation).
public abstract class MatrixForm extends FormSubject {

    protected MatrixHandler handler;

    public MatrixForm(MatrixHandler handler) {
        this.handler = handler;
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public void setHandler(MatrixHandler handler) {
        this.handler = handler;
    }

    public void start() {
        sendUpdate(FormEvent.PROCESSING_START);
        process();
        sendUpdate(FormEvent.PROCESSING_END);
    }

    protected void sendUpdate(int type) {
        update(new FormEvent(type, handler));
    }

    protected abstract void process();

}