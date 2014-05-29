package rs.etf.km123247m.Parser.MatrixParser;

import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;
import rs.etf.km123247m.Properties.PropertyManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public abstract class MatrixStringParser extends StringParser {

    @Override
    protected void preInputParseChecks(String inputString) throws Exception {
        if(this.getInputString() == null) {
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
    protected IMatrix generateObject(String input) {
        IMatrix matrix = null;
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        try {
            Pattern rowPattern = Pattern.compile("(.*?);");
            Matcher rowMatcher = rowPattern.matcher(this.getInputString());

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
            int min = Integer.parseInt(PropertyManager.getProperty("min_degree"));
            int max = Integer.parseInt(PropertyManager.getProperty("max_degree"));
            if (min > list.size() || max < list.size()) {
                throw new Exception(("Wrong matrix size!") + list.size());
            }

            matrix = new ArrayMatrix(list.size(), list.get(0).size());

            for (i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).size(); j++) {
                    Object o = createMatrixElement(list.get(i).get(j));
                    matrix.set(new MatrixCell(j, i, o));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return matrix;
    }

    protected abstract Object createMatrixElement(String s) throws Exception;

    @Override
    protected abstract void postObjectGenerationChecks(Object o) throws Exception;
}
