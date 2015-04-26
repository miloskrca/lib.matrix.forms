package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.EJMLPolynomialHandler;
import rs.etf.km123247m.Matrix.Handler.Implementation.PolynomialMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Matrix.MatrixCell;
import rs.etf.km123247m.Observer.Event.FormEvent;
import rs.etf.km123247m.Observer.FormObserver;
import rs.etf.km123247m.Polynomial.Term;
import rs.etf.km123247m.PropertyManager.PropertyManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;

/**
 * Created by Miloš Krsmanović.
 * Jun 2014
 * <p/>
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class JordanMatrixForm extends MatrixForm implements FormObserver {

    /**
     * Matrix transformed to Rational-Canonical form.
     */
    private IMatrix startMatrix;

    /**
     * Transitional matrix.
     */
    private IMatrix transitionalMatrix;

    /**
     * Final result of the transformation.
     */
    private IMatrix finalMatrix;

    /**
     * Found roots that will be transformed to factors
     */
    private ArrayList<ArrayList<Object>> roots = new ArrayList<ArrayList<Object>>();

    /**
     * Constructor
     *
     * @param handler Handler
     * @throws Exception
     */
    public JordanMatrixForm(MatrixHandler handler) throws Exception {
        super(handler);
        startMatrix = handler.duplicate(handler.getMatrix());
        int rowNumber = startMatrix.getRowNumber();
        int columnNumber = startMatrix.getColumnNumber();
        finalMatrix = startMatrix.createMatrix(rowNumber, columnNumber);
        Object zero = ((PolynomialMatrixHandler) handler).getZero();
        finalMatrix.initWith(zero);
        transitionalMatrix = handler.diagonal(startMatrix.getRowNumber(), getHandler().getObjectFromString(String.valueOf(Term.X)));
    }

    @Override
    protected void process() throws Exception {
        getHandler().setMatrix(startMatrix);
        if (getHandler().matrixContainsSymbol()) {
            sendUpdate(FormEvent.PROCESSING_EXCEPTION, FormEvent.EXCEPTION_MATRIX_NOT_NUMERICAL, null);
            return;
        }

        sendUpdate(FormEvent.PROCESSING_START, null, getStartMatrix());

        transitionalMatrix = getHandler().subtract(transitionalMatrix, startMatrix);
        getHandler().setMatrix(transitionalMatrix);
        sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_SUBTRACT_FOR_SMITH, getHandler().getMatrix());

        MatrixForm form = new SmithMatrixForm(getHandler());
        form.addObserver(this);
        form.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        FormEvent event = (FormEvent) arg;
        MatrixForm smithMatrixForm = (MatrixForm) o;
        if (event.getType() == FormEvent.PROCESSING_STEP) {
            this.getCommands().add(smithMatrixForm.getCommands().getLast());
            // pass the event
            sendUpdate(event.getType(), event.getMessage(), event.getMatrix());
        } else if (event.getType() == FormEvent.PROCESSING_START) {
            // ignore
        } else if (event.getType() == FormEvent.PROCESSING_EXCEPTION
                || event.getType() == FormEvent.PROCESSING_INFO) {
            // pass the event
            sendUpdate(event.getType(), event.getMessage(), event.getMatrix());
        } else if (event.getType() == FormEvent.PROCESSING_END) {
            // Smith transformation ended
            try {
                sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_JORDAN_GENERATE_BLOCKS, transitionalMatrix);
                generateRootsFactorsAndBlocks();
                sendUpdate(FormEvent.PROCESSING_INFO, FormEvent.INFO_JORDAN_END_GENERATE_BLOCKS, transitionalMatrix);
                sendUpdate(FormEvent.PROCESSING_END, null, getFinalMatrix());
            } catch (Exception e) {
                e.printStackTrace();
                sendUpdate(FormEvent.PROCESSING_EXCEPTION, e.getMessage(), event.getMatrix());
            }
        }
    }

    /**
     * Generates roots, factors and Jordan blocks
     *
     * @throws Exception
     */
    protected void generateRootsFactorsAndBlocks() throws Exception {
        PolynomialMatrixHandler handler = ((PolynomialMatrixHandler) getHandler());
        for (int row = 0; row < transitionalMatrix.getRowNumber(); row++) {
            for (int column = 0; column < transitionalMatrix.getColumnNumber(); column++) {
                Object currentElement = transitionalMatrix.get(row, column).getElement();
                if (handler.compare(currentElement, handler.getZero()) != 0
                        && handler.compare(currentElement, handler.getOne()) != 0) {

                    ArrayList<Object> currentFactors = handler.factor(currentElement);
                    if (currentFactors.size() == 0) {
                        throw new Exception("No factors generated for polynomial " + currentElement);
                    }

                    String factorsString = "";
                    if (!((SymJaMatrixHandler) handler).getIExprFactorisation().isFactorisationCorrect()
                            && PropertyManager.getProperty("use_EJML").equals("1")) {
                        factorsString = generateFactorsStringUsingEJMLAndAddRoots(currentElement);
                    } else {
                        roots.add(handler.getRoots(currentElement));
                        for (Object factor : currentFactors) {
                            /*if (factor.toString().startsWith("(")) {
                                // not precise enough
                                factorsString += "*" + factor;
                            } else */if (factor.toString().equals(String.valueOf(Term.X))) {
                                factorsString += "*" + factor.toString();
                            } else {
                                factorsString += "*(" + factor + ")";
                            }
                        }
                        // left trim one * symbol
                        factorsString = factorsString.substring(1);
                    }
                    // Save factors in transitional matrix for presentational use
                    // For actual generation of blocks, roots are used
                    transitionalMatrix.set(new MatrixCell(row, column, factorsString));
                }
            }
        }

        for (ArrayList<Object> rootsItem : roots) {
            // sort all roots so we can check for re-occurring ones
            Collections.sort(rootsItem, new Comparator<Object>() {
                public int compare(Object root1, Object root2) {
                    return root1.toString().compareTo(root2.toString());
                }
            });
        }

        int rootsIndex = 0;
        int row = 0;
        while (row < finalMatrix.getRowNumber()) {
            ArrayList<Object> rootsForBuilding = roots.get(rootsIndex);
            int rootsForBuildingIndex = 0;
            while (rootsForBuildingIndex < rootsForBuilding.size()) {
                // Detect repeating roots
                // easy to iterate since we sorted the roots the previously
                int power = Collections.frequency(rootsForBuilding, rootsForBuilding.get(rootsForBuildingIndex));
                Object root = rootsForBuilding.get(rootsForBuildingIndex);
                setBlockFromRoot(row, power, root);
                rootsForBuildingIndex += power;
                row += power;
            }
            rootsIndex++;
        }
    }

    protected String generateFactorsStringUsingEJMLAndAddRoots(Object currentElement) throws Exception {
        // EJML way
        PolynomialMatrixHandler handler = ((PolynomialMatrixHandler) getHandler());
        EJMLPolynomialHandler polyHandler = new EJMLPolynomialHandler();
        int highestPower = handler.getHighestPower(currentElement);
        double[] coefficients = new double[highestPower + 1];
        for (int i = 0; i <= highestPower; i++) {
            coefficients[i] = Double.parseDouble(handler.getCoefficientForPower(currentElement, i).toString());
        }

        Object[] tempRoots = polyHandler.findRoots(coefficients);
        ArrayList<Object> arrayListRoots = new ArrayList<Object>();
        Collections.addAll(arrayListRoots, polyHandler.toRational(tempRoots));
        roots.add(arrayListRoots);

        return polyHandler.rootsToFactors(tempRoots);
    }

//    /**
//     * Generates roots, factors and Jordan blocks
//     *
//     * @throws Exception
//     */
//    protected void generateRootsFactorsAndBlocks() throws Exception {
//        PolynomialMatrixHandler handler = ((PolynomialMatrixHandler) getHandler());
//        EJMLPolynomialHandler polyHandler = new EJMLPolynomialHandler();
//        for (int row = 0; row < transitionalMatrix.getRowNumber(); row++) {
//            for (int column = 0; column < transitionalMatrix.getColumnNumber(); column++) {
//                Object currentElement = transitionalMatrix.get(row, column).getElement();
//                if (handler.compare(currentElement, handler.getZero()) != 0
//                        && handler.compare(currentElement, handler.getOne()) != 0) {
//
//                    // First try to do it using SymJa
//                    // SymJa way
//                    Object factoredCurrentElement = handler.factor(currentElement);
//                    String factorsString;
//
//                    if (!currentElement.toString().equals(factoredCurrentElement.toString()) && !factoredCurrentElement.toString().contains(Term.X + "^")) {
//                        factorsString = factoredCurrentElement.toString();
//                        factors.addAll(((PolynomialMatrixHandler) getHandler()).getFactorsFromElement(factoredCurrentElement));
//                    } else {
//                        // Factorization using SymJa failed
//                        int highestPower = handler.getHighestPower(currentElement);
//                        switch (highestPower) {
//                            case -2:
//                                // "By hand" way using quadratic formula
//                                // NOT USED
//                                Object[] qRoots = ((PolynomialMatrixHandler) getHandler()).quadraticFormula(currentElement);
//                                Collections.addAll(roots, qRoots);
//                                factorsString = handler.getFactorsStringFromRoots(qRoots);
//                                break;
//                            default:
//                                // EJML way
//                                double[] coefficients = new double[highestPower + 1];
//                                for (int i = 0; i <= highestPower; i++) {
//                                    coefficients[i] = Double.parseDouble(handler.getCoefficientForPower(currentElement, i).toString());
//                                }
//
//                                Object[] tempRoots = polyHandler.findRoots(coefficients);
//                                Collections.addAll(roots, polyHandler.toRational(tempRoots));
//                                factorsString = polyHandler.rootsToFactors(tempRoots);
//                        }
//                    }
//
//                    // Save factors in transitional matrix for presentational use
//                    // For actual generation of blocks, roots are used
//                    transitionalMatrix.set(new MatrixCell(row, column, factorsString));
//                }
//            }
//        }
//
//        if (factors.size() > 0) {
//            // SymJa way
//            generateBlocks();
//        } else {
//            // EJML way
//
//            // sort all roots so we can check for re-occurring ones
//            Collections.sort(roots, new Comparator<Object>() {
//                public int compare(Object root1, Object root2) {
//                    return root1.toString().compareTo(root2.toString());
//                }
//            });
//
//            int row = 0;
//            while (row < finalMatrix.getRowNumber()) {
//                int power = 1;
//                // Detect repeating roots when not using CoefficientPowerPair
//                while (roots.size() > row + power
//                        && roots.get(row).toString().equals(roots.get(row + power).toString())) {
//                    power++;
//                }
//
//                if (power > 1) {
//                    // add ones in diagonal above the main diagonal
//                    // but only inside the current block
//                    for (int i = row; i < row + power - 1; i++) {
//                        Object one = handler.getOne();
//                        finalMatrix.set(new MatrixCell(i, i + 1, one));
//                    }
//                }
//                for (int i = row; i < row + power; i++) {
//                    finalMatrix.set(new MatrixCell(i, i, roots.get(row)));
//                }
//
//                row += power;
//            }
//        }
//    }
//
//    /**
//     * Generate blocks from factors and store them in final matrix
//     *
//     * @throws Exception
//     */
//    private void generateBlocks() throws Exception {
//        final PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
//        //generate a list of power/coefficient pairs
//        ArrayList<CoefficientPowerPair> pairs = new ArrayList<CoefficientPowerPair>();
//        for (Object factor : factors) {
//            CoefficientPowerPair pair = handler.getCoefficientPowerPairFromFactor(factor);
//            Object coefficient = pair.getCoefficient();
//            pair.setCoefficient(handler.calculateNegativeElement(coefficient));
//            pairs.add(pair);
//            if (!coefficient.toString().equals("0")) {
//                for (int i = 0; i < Integer.parseInt(pair.getPower().toString()); i++) {
//                    roots.add(handler.calculateNegativeElement(coefficient).toString());
//                }
//            }
//        }
//
//        int index = 0;
//        int row = 0;
//        while (row < finalMatrix.getRowNumber()) {
//            int power = Integer.parseInt(pairs.get(index).getPower().toString());
//            setBlock(row, power, pairs.get(index));
//            row += power;
//            index++;
//        }
//    }

    /**
     * Generates and adds a block to final matrix.
     *
     * @param startRow Row the block starts from.
     * @param size     Size of the block.
     * @param root     Object gotten from
     *                 corresponding polynomial from which the block is generated.
     * @throws Exception
     */
    protected void setBlockFromRoot(int startRow, int size, Object root) throws Exception {
        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
        if (size > 1) {
            // add ones in diagonal above the main diagonal
            // but only inside the current block
            for (int row = startRow; row < startRow + size - 1; row++) {
                finalMatrix.set(new MatrixCell(row, row + 1, handler.getOne()));
            }
        }
        for (int row = startRow; row < startRow + size; row++) {
            finalMatrix.set(new MatrixCell(row, row, root));
        }
    }

//    /**
//     * Generates and adds a block to final matrix.
//     *
//     * @param startRow  Row the block starts from.
//     * @param size      Size of the block.
//     * @param blockPair Coefficient-Power pars gotten from
//     *                  corresponding polynomial from which the block is generated.
//     * @throws Exception
//     */
//    protected void setBlock(int startRow, int size, CoefficientPowerPair blockPair) throws Exception {
//        PolynomialMatrixHandler handler = (PolynomialMatrixHandler) getHandler();
//        if (size > 1) {
//            // add ones in diagonal above the main diagonal
//            // but only inside the current block
//            for (int row = startRow; row < startRow + size - 1; row++) {
//                Object one = handler.getOne();
//                finalMatrix.set(new MatrixCell(row, row + 1, one));
//            }
//        }
//        for (int row = startRow; row < startRow + size; row++) {
//            finalMatrix.set(new MatrixCell(row, row, blockPair.getCoefficient()));
//        }
//    }

    /**
     * Matrix to be transformed to rational-canonical form.
     *
     * @return IMatrix
     */
    public IMatrix getStartMatrix() {
        return startMatrix;
    }

    /**
     * Final transitional matrix.
     *
     * @return IMatrix
     */
    public IMatrix getTransitionalMatrix() {
        return transitionalMatrix;
    }

    /**
     * Final transformed matrix.
     *
     * @return IMatrix
     */
    public IMatrix getFinalMatrix() {
        return finalMatrix;
    }

    /**
     * Roots
     *
     * @return ArrayList<Object>
     */
    public ArrayList<ArrayList<Object>> getRoots() {
        return roots;
    }
}
