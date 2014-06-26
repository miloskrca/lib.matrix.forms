package rs.etf.km123247m.Parser.ParserTypes;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.IParser
 */
public abstract class StringParser implements IParser {

    private String inputString;

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    @Override
    public Object parseInput() throws Exception {
        preInputParseChecks(getInputString());
        String parsedInput = parseFromInput(getInputString());
        postInputParseChecks(parsedInput);
        Object o = generateObject(parsedInput);
        o = postObjectGeneration(o);
        return o;
    }

    protected abstract void preInputParseChecks(String inputString) throws Exception;
    protected abstract String parseFromInput(String inputString);
    protected abstract void postInputParseChecks(String parsedInput) throws Exception;
    protected abstract Object generateObject(String input) throws Exception;
    protected abstract Object postObjectGeneration(Object o) throws Exception;

}