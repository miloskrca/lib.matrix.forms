package rs.etf.km123247m.Tests;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Observers.FormObserver;
import rs.etf.km123247m.Observers.FormSubject;

import java.util.Observable;

/**
 * Created by Miloš Krsmanović.
 * 2014
 *
 * package: rs.etf.km123247m.Tests
 */

/**
 * Simple implementation of the Form class
 */
class ConcreteForm extends FormSubject {
    private int id;
    public ConcreteForm(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    public void update() {
        update(null);
    }
}

class ConcreteFormObserver extends FormObserver {
    private int value = 0;
    private FormSubject subject;

    @Override
    public void update(Observable o, Object arg) {
        subject = (FormSubject)o;
        this.value++;
    }

    int getValue() {
        return value;
    }

    ConcreteForm getSubject() {
        return (ConcreteForm) this.subject;
    }
}


public class FormSubjectObservableTest {
    private ConcreteForm form;
    private ConcreteFormObserver observer;
    private int id = 123456;

    @Before
    public void setUp() throws Exception {
        this.form = new ConcreteForm(this.id);
        this.observer = new ConcreteFormObserver();
        this.form.addObserver(this.observer);
    }

    @Test
    public void testSetChanged() throws Exception {
        this.form.update();
        assert this.id == this.observer.getSubject().getId();
        assert 1 == this.observer.getValue();
    }
}
