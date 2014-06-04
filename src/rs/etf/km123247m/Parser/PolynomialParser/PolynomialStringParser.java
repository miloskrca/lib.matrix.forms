package rs.etf.km123247m.Parser.PolynomialParser;

import rs.etf.km123247m.Parser.ParserTypes.StringParser;
import rs.etf.km123247m.Polynomial.Polynomial;
import rs.etf.km123247m.Polynomial.Term;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class PolynomialStringParser extends StringParser {

    // Variables
    short sign;
    int pow;
    char var;
    int coefficient;

    @Override
    protected void preInputParseChecks(String inputString) throws Exception {
        if(inputString == null) {
            throw new Exception("No matrix string to parse");
        }
    }

    @Override
    protected String parseFromInput(String inputString) {
        // do nothing
        return inputString;
    }

    @Override
    protected void postInputParseChecks(String parsedInput) throws Exception {
        // do nothing
    }

    @Override
    protected Object generateObject(String input) throws Exception {
        Polynomial poly = new Polynomial();
        input = input.replace("-", "+-");
        String[] terms = input.split("\\+");

        // parse string
        for (String term : terms) {
            clearVariables();
            sign = term.charAt(0) == Term.MINUS_CHAR ? Term.MINUS : Term.PLUS;
            if(term.charAt(0) == Term.MINUS_CHAR || term.charAt(0) == Term.PLUS_CHAR) {
                term = term.substring(1);
            }
            if(term.contains("^")) {
                String[] parts = term.split("\\^");
                if(parts.length == 2) {
                    if(isNumeric(parts[1])) {
                        pow = Integer.parseInt(parts[1]);
                    } else {
                        throw new Exception("qurac2");
                    }
                    handleCoefficientAndVar(parts[0]);
                } else {
                    throw new Exception("qurac");
                }
            } else {
                handleCoefficientAndVar(term);
            }

            Term termObject = new Term(sign, coefficient, var, pow);
            poly.addTerm(termObject);
        }
        return poly;
    }

    private void clearVariables() {
        sign = Term.PLUS;
        pow = 1;
        var = Term.NULL;
        coefficient = 1;
    }

    private void handleCoefficientAndVar(String parts) throws Exception {
        if((parts.length() == 1)) {
            if(Character.isLetter(parts.charAt(0))) {
                var = parts.charAt(0);
            } else if(Character.isDigit(parts.charAt(0))) {
                coefficient = Integer.parseInt(String.valueOf(parts.charAt(0)));
            } else {
                throw new Exception("qurac3");
            }
        } else {
            if(parts.contains("*")) {
                String[] subParts = parts.split("\\*");
                if(subParts.length == 2) {
                    if(isNumeric(subParts[0])) {
                        coefficient = Integer.parseInt(subParts[0]);
                    } else {
                        throw new Exception("qurac7");
                    }
                    if(subParts[1].length() == 1) {
                        var = subParts[1].charAt(0);
                    } else {
                        throw new Exception("qurac6");
                    }
                } else {
                    // TODO: We are getting this parts = "34*x*3" and cannot parse it, fix it.
                    throw new Exception("qurac5");
                }
            } else if(isNumeric(parts)) {
                coefficient = Integer.parseInt(parts);
            } else {
                throw new Exception("qurac4");
            }
        }
    }


    private boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    @Override
    protected void postObjectGenerationChecks(Object o) throws Exception {
        // do nothing
    }
}