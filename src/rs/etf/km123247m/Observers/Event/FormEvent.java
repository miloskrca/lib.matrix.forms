package rs.etf.km123247m.Observers.Event;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Observers
 */
public class FormEvent {

    public static final int PROCESSING_START = 1;
    public static final int PROCESSING_STATUS = 2;
    public static final int PROCESSING_END = 3;
    public static final int PROCESSING_EXCEPTION = 4;

    private int type;

    private Object object;

    private String message;

    public FormEvent(int type, Object object, String message) {
        this.type = type;
        this.object = object;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
