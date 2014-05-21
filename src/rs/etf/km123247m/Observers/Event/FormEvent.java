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

    private int type;

    private Object object;

    public FormEvent(int type, Object object) {
        this.type = type;
        this.object = object;
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
}
