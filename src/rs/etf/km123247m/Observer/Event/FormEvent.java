package rs.etf.km123247m.Observer.Event;

import rs.etf.km123247m.Matrix.IMatrix;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Observer
 */
public class FormEvent {

    // message types
    public static final int PROCESSING_START = 1;
    public static final int PROCESSING_STEP = 2;
    public static final int PROCESSING_INFO = 3;
    public static final int PROCESSING_END = 4;
    public static final int PROCESSING_EXCEPTION = 5;

    // message codes
    public static final String INFO_FIX_ELEMENTS_ON_DIAGONAL = "INFO_FIX_ELEMENTS_ON_DIAGONAL";
    public static final String INFO_END_FIX_ELEMENTS_ON_DIAGONAL = "INFO_END_FIX_ELEMENTS_ON_DIAGONAL";
    public static final String INFO_FIX_LEADING_COEFFICIENTS = "INFO_FIX_LEADING_COEFFICIENTS";
    public static final String INFO_END_FIX_LEADING_COEFFICIENTS = "INFO_END_FIX_LEADING_COEFFICIENTS";
    // rational message codes
    public static final String INFO_RATIONAL_FINISH_RATIONAL_START_T = "INFO_RATIONAL_FINISH_RATIONAL_START_T";
    public static final String INFO_SUBTRACT_FOR_SMITH = "INFO_SUBTRACT_FOR_SMITH";
    // jordan message codes
    public static final String INFO_JORDAN_GENERATE_FACTORS = "INFO_JORDAN_GENERATE_FACTORS";
    public static final String INFO_JORDAN_GENERATE_BLOCKS = "INFO_JORDAN_GENERATE_BLOCKS";
    public static final String INFO_JORDAN_END_GENERATE_BLOCKS = "INFO_JORDAN_END_GENERATE_BLOCKS";

    // exception message codes
    public static final String EXCEPTION_MATRIX_IS_SINGULAR = "EXCEPTION_MATRIX_IS_SINGULAR";
    public static final String EXCEPTION_MATRIX_NOT_NUMERICAL = "EXCEPTION_MATRIX_NOT_NUMERICAL";

    private final IMatrix matrix;

    private int type;

    private String message;

    public FormEvent(int type, String message, IMatrix matrix) {
        this.type = type;
        this.message = message;
        this.matrix = matrix;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public IMatrix getMatrix() {
        return matrix;
    }
}
