package rs.etf.km123247m.Parser.MatrixParser.SymJa;

import rs.etf.km123247m.Parser.MatrixParser.MatrixFileParser;
import rs.etf.km123247m.Parser.ParserTypes.StringParser;

import java.io.File;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Parser
 */
public class IExprMatrixFileParser extends MatrixFileParser {

    private boolean nativeInput;

    /**
     * File parser that parses a matrix with elements in IExpr form
     * @param file File
     */
    public IExprMatrixFileParser(File file) {
        super(file);
        this.nativeInput = true;
    }

    /**
     * File parser that parses a matrix with elements in Non-native or Native(IExpr) form.
     * By default it parses in the Native form.
     * e.g.
     * Native: Plus[Times[-1, x], Power[x, 2]]
     * Non-native: -x+x^2
     * @param file File
     * @param nativeInput Is native input
     */
    public IExprMatrixFileParser(File file, boolean nativeInput) {
        super(file);
        this.nativeInput = nativeInput;
    }

    @Override
    protected StringParser instantiateStringParser() {
        return new IExprMatrixStringParser(nativeInput);
    }

    public boolean isNativeInput() {
        return nativeInput;
    }

    public void setNativeInput(boolean nativeInput) {
        this.nativeInput = nativeInput;
    }
}
