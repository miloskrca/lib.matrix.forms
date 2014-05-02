package rs.etf.km123247m.Parser;

import rs.etf.km123247m.Matrix.Matrix;
import rs.etf.km123247m.Polinomial.Polynomial;
import rs.etf.km123247m.Polinomial.Term;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.IParser
 */
public class StringParser implements IParser {

    private String matrixString;

    public String getMatrixString() {
        return matrixString;
    }

    public void setMatrixString(String matrixString) {
        this.matrixString = matrixString;
    }

    @Override
    public Matrix parseMatrix() throws Exception {
        if(this.getMatrixString() == null) {
            throw new Exception("No matrix string to parse");
        }

        Matrix matrix = null;

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        try {
            Pattern rowPattern = Pattern.compile("(.*?);");
            Matcher rowMatcher = rowPattern.matcher(this.getMatrixString());

            int i = 0;
            while (rowMatcher.find()) {
                list.add(new ArrayList<String>());
                String[] elemArray = rowMatcher.group().split(",");
                for (int j = 0; j < elemArray.length; j++) {
                    elemArray[j] = elemArray[j].replace(";", "").replace(" ",
                            "");
                    list.get(i).add(elemArray[j]);
                }
                i++;
            }

            for (i = 0; i < list.size(); i++) {
                if (list.size() != list.get(i).size())
                    throw new Exception("Matrix format error! m != n");
            }
            if (2 > list.size() || 4 < list.size()) {
                throw new Exception(("Wrong matrix size!") + list.size());
            }

            matrix = new Matrix<Polynomial>(list.size(), list.get(0).size());

            for (i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).size(); j++) {
                    Polynomial p = new Polynomial();
                    Term t = new Term(Term.MINUS, 1, "x", 2);
                    p.addTerm(t);
                    matrix.set(i, j, p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return matrix;
    }
}
