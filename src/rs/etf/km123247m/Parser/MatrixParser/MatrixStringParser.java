package rs.etf.km123247m.Parser.MatrixParser;

import rs.etf.km123247m.Matrix.Implementation.ArrayMatrix;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;
import rs.etf.km123247m.PropertyManager.PropertyManager;

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

    /**
     *
     */
    private int min_power = 2;

    /**
     *
     */
    private int max_power = 4;

    @Override
    protected void preInputParseChecks(String inputString) throws Exception {
        if (inputString == null) {
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
    protected IMatrix generateObject(String input) throws Exception {
        IMatrix matrix;
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

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
        String sMin = PropertyManager.getProperty("min_power");
        if(sMin != null) {
            min_power = Integer.parseInt(sMin);
        }
        String sMax = PropertyManager.getProperty("max_power");
        if(sMax != null) {
            max_power = Integer.parseInt(sMax);
        }
        if (min_power > list.size()) {
            throw new Exception(("Wrong matrix size! Matrix too small."));
        }
        if (max_power < list.size()) {
            throw new Exception(("Wrong matrix size! Matrix too big."));
        }

        matrix = new ArrayMatrix(list.size(), list.get(0).size());

        for (i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                Object o = createMatrixElement(list.get(i).get(j));
                matrix.set(new MatrixCell(i, j, o));
            }
        }

        return matrix;
    }

    protected abstract Object createMatrixElement(String s) throws Exception;

    @Override
    protected abstract Object postObjectGeneration(Object o) throws Exception;
}
