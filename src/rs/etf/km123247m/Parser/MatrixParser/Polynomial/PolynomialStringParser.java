package rs.etf.km123247m.Parser.MatrixParser.Polynomial;

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
    String coefficient;

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
        input = input.replace("-", "+-").replace(" ", "");
        if(input.charAt(0) == Term.PLUS_CHAR) {
            // if the input starts with a "+" sign, remove it, so the split()
            // doesn't return an empty first element
            input = input.substring(1);
        }
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
                        throw new Exception("Exception while parsing: degree not numerical!");
                    }
                    handleCoefficientAndVar(parts[0]);
                } else {
                    throw new Exception("Exception while parsing: power has more than two parts!");
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
        coefficient = "1";
    }

    private void handleCoefficientAndVar(String parts) throws Exception {
        if((parts.length() == 1) || isNumeric(parts)) {
            if(Character.isLetter(parts.charAt(0))) {
                var = parts.charAt(0);
            } else if(isDouble(parts)) {
                coefficient = String.valueOf(parts);
                pow = 0;
            } else if (Character.isDigit(parts.charAt(0))) {
                coefficient = String.valueOf(parts.charAt(0));
                pow = 0;
            } else {
                throw new Exception("Exception while parsing: unknown value!");
            }
        } else {
            if(parts.contains("*")) {
                String[] subParts = parts.split("\\*");
                if(subParts.length == 2) {
                    if(isNumeric(subParts[0])) {
                        coefficient = subParts[0];
                    } else {
                        throw new Exception("Exception while parsing: multiplication part not numerical!");
                    }
                    if(subParts[1].length() == 1) {
                        var = subParts[1].charAt(0);
                    } else {
                        throw new Exception("Exception while parsing: multiplication has too many elements!");
                    }
                } else {
                    throw new Exception("Exception while parsing: multiplication has too many elements!");
                }
            } else if(isNumeric(parts)) {
                coefficient = parts;
            } else {
                throw new Exception("Exception while parsing: unrecognized operation!");
            }
        }
    }

    private boolean isNumeric(String s) {
        return s.matches(("^([\\+\\-]?\\d+)$")) || isDouble(s) || isFraction(s);
    }

    private boolean isFraction(String s) {
        return s.matches(("^([\\+\\-]?\\d+)/([\\+\\-]?\\d+)$"));
    }

    private boolean isDouble(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    @Override
    protected Object postObjectGeneration(Object o) throws Exception {
        return o;
    }
}