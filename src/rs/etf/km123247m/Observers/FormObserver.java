package rs.etf.km123247m.Observers;

import java.util.Observable;
import java.util.Observer;

/**
 * Form Observer
 */
public abstract class FormObserver implements Observer {

    /**
     * Updates observer of the Form
     *
     * @param o FormSubject
     * @param arg Form
     */
    @Override
    public abstract void update(Observable o, Object arg);
}
