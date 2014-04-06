package rs.etf.km123247m.Observers;

import java.util.Observable;

/**
 * Form Subject
 * Form should extend this class so the GUI can observe the calculations in the form
 */
public abstract class FormSubject extends Observable {
    /**
     * Set the 'changed' flag in Observable parent
     */
    public void setChanged() {
        super.setChanged();
    }
}
