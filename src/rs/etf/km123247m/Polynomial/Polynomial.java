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

    public static Polynomial getZeroPolynomial() {
        return new Polynomial(new Term[] {
            Term.getZeroTerm()
        });
    }

    @Override
    public int compareTo(Object o) {
        Polynomial p = (Polynomial) o;
        ArrayList<Term> oTerms = p.getTerms();

        int highestTermPower = 0;
        for (Term term : terms) {
            if(highestTermPower < term.getPower()) {
                highestTermPower = term.getPower();
            }
        }
        int highestOTermPower = 0;
        for (Term oTerm : oTerms) {
            if(highestOTermPower < oTerm.getPower()) {
                highestOTermPower = oTerm.getPower();
            }
        }

        if(highestTermPower != highestOTermPower) {
            return highestTermPower > highestOTermPower ? 1 : -1;
        }

        if(oTerms.size() != terms.size()) {
            return terms.size() > oTerms.size() ? 1 : -1;
        }

        int compareCount = 0;
        int compareResult;
        boolean found;
        for (Term oTerm : oTerms) {
            found = false;
            for (Term term : terms) {
                compareResult = term.compareTo(oTerm);
                if(compareResult == 0) {
                    found = true;
                    break;
                } else {
                    compareCount += compareResult;
                }
            }
            if(!found) {
                return compareCount == 0 ? 1 : (compareCount > 0 ? 1 : -1);
            }
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
