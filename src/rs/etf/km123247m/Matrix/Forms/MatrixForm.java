package rs.etf.km123247m.Matrix.Forms;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Observers.Event.FormEvent;
import rs.etf.km123247m.Observers.FormSubject;
import java.util.LinkedList;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Forms
 */
public abstract class MatrixForm extends FormSubject {

    private MatrixHandler handler;
    private LinkedList<ICommand> commands;

    public MatrixForm(MatrixHandler handler) {
        this.handler = handler;
        this.commands = new LinkedList<ICommand>();
    }

    public MatrixHandler getHandler() {
        return handler;
    }

    public void setHandler(MatrixHandler handler) {
        this.handler = handler;
    }

    public void start() {
        try {
            sendUpdate(FormEvent.PROCESSING_START, null);
            process();
            sendUpdate(FormEvent.PROCESSING_END, null);
        } catch (Exception exception) {
            sendUpdate(FormEvent.PROCESSING_EXCEPTION, exception.getMessage());
        }
    }

    protected void sendUpdate(int type, String message) {
        update(new FormEvent(type, handler, message));
    }

    public LinkedList<ICommand> getCommands() {
        return commands;
    }

    protected abstract void process() throws Exception;

}