package rs.etf.km123247m.Tests.ObserverTest;

import org.junit.Before;
import org.junit.Test;
import rs.etf.km123247m.Observer.FormObserver;
import rs.etf.km123247m.Observer.FormSubject;

import java.util.Observable;

/**
 * Created by Miloš Krsmanović.
 * 2014
 *
 * package: rs.etf.km123247m.Tests
 */

public class FormSubjectObservableTest {

    /**
     * Simple implementation of the Form class
     */
    private class ConcreteForm extends FormSubject {
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

    private class ConcreteFormObserver extends FormObserver {
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
