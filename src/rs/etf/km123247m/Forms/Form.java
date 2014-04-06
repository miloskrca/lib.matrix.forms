package rs.etf.km123247m.Forms;

import rs.etf.km123247m.Observers.FormSubject;

/**
 * Form
 */
public abstract class Form extends FormSubject{
    /**
     * Notifies observers of an update
     */
    public void update() {
        super.setChanged();
        synchronized (this) {
            this.notifyObservers(null);
        }
    }
}
