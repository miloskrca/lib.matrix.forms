package rs.etf.km123247m.Polynomial;

import java.util.ArrayList;

/**
 * Created by Miloš Krsmanović.
 * 2014
 *
 * package: rs.etf.km123247m.Polynomial
 */
public class Polynomial implements Comparable {
    private ArrayList<Term> terms;

    public Polynomial() {
        this.terms = new ArrayList<Term>();
    }

    public Polynomial(Term[] terms) {
        this();
        for (Term term : terms) {
            this.addTerm(term);
        }
    }

    public Polynomial(ArrayList<Term> terms) {
        this();
        this.terms.addAll(terms);
    }

    public Polynomial addTerm(Term term) {
        this.terms.add(term);

        return this;
    }

    public Term getTerm(int index) throws Exception {
        return terms.get(index);
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    //TODO: do a proper compare
    @Override
    public int compareTo(Object o) {
        Polynomial p = (Polynomial) o;
        ArrayList<Term> oTerms = p.getTerms();
        if(oTerms.size() != terms.size()) {
            return oTerms.size() > terms.size() ? -1 : 1;
        }
        return 0;
    }

    public String toString() {
        String polynomialString = "";
        for(Term term : terms) {
            polynomialString += term.getSignChar() + term.toString();
        }

        return polynomialString.substring(1);
    }
}
