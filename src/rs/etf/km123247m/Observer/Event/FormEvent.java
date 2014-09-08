package rs.etf.km123247m.Observer.Event;

import rs.etf.km123247m.Matrix.IMatrix;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Observer
 */
public class FormEvent {

    public static final int PROCESSING_START = 1;
    public static final int PROCESSING_STEP = 2;
    public static final int PROCESSING_INFO = 3;
    public static final int PROCESSING_END = 4;
    public static final int PROCESSING_EXCEPTION = 5;
    private final IMatrix matrix;

    private int type;

    private String message;

    public FormEvent(int type, String message, IMatrix matrix) {
        this.type = type;
        this.message = message;
        this.matrix = matrix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IMatrix getMatrix() {
        return matrix;
    }
}
