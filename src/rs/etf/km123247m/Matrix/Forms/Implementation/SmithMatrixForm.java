package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Command.ICommand;
import rs.etf.km123247m.Command.MatrixCommand.AddPolynomialToCellAndSaveCommand;
import rs.etf.km123247m.Command.MatrixCommand.DivideCellsAndReturnCommand;
import rs.etf.km123247m.Command.MatrixCommand.MultiplyPolynomialWithCellAndReturnCommand;
import rs.etf.km123247m.Command.MatrixCommand.NegatePolynomialAndReturnCommand;
import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Matrix.IMatrix;
import rs.etf.km123247m.Observers.Event.FormEvent;
import rs.etf.km123247m.Polynomial.Polynomial;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
public class SmithMatrixForm extends MatrixForm {

    public SmithMatrixForm(MatrixHandler handler) {
        super(handler);
    }

    @Override
    public void process() throws Exception{
        sendUpdate(FormEvent.PROCESSING_STATUS);

        MatrixHandler handler = this.getHandler();
        int matrixSize = handler.getMatrix().getRowNumber();

        // Started processing on level subMatrixLvl
        for (int subMatrixLvl = 0; subMatrixLvl < matrixSize - 1; subMatrixLvl++) {
            do {
                do {
                    // Moving smallest to start...
                    moveSmallestToStart(subMatrixLvl, matrixSize);
                    for (int row2 = subMatrixLvl + 1; row2 < matrixSize; row2++) {
                        DivideCellsInColumn(subMatrixLvl, row2, subMatrixLvl, matrixSize);
                    }

                } while (!isColumnCleared(subMatrixLvl));
                // Finished with column

                for (int column2 = subMatrixLvl + 1; column2 < matrixSize; column2++) {
                    processColumns(subMatrixLvl, column2, subMatrixLvl, matrixSize);
                }
                // Finished with row
            } while (!isRowCleared(subMatrixLvl));
            // Done with the level subMatrixLvl
        }
        // Checking elements on the diagonal...
        if (!isDiagonalOk()) {
            // Diagonal not ok. Fixing.
            for (int row = 0; row < matrixSize - 1; row++) {
                if (needsFixing(row)) {
                    //noinspection UnnecessaryLocalVariable
                    int column = row;
                    // Adding row " + (row + 1) + " to " + row
                    addRows(row, row + 1);
                    do {
                        do {
                            // MoveSmallestToStart
                            moveSmallestToStart(row, row + 2);
                            // "processColumns = row " + column);
                            processColumns(column, column + 1, row, row + 2);
                        } while (!isRowCleared(row));
                        // "DivideCellsInColumn = column  " + row);
                        DivideCellsInColumn(row, row + 1, row, row + 2);
                    } while (!isColumnCleared(column));
                }
            }
        }

        // Fix element on position [0, 0] if needed...
        fixFirstElement();
        // Finished
    }
    /**
     *    if(matrix[row2, column] != Poly(0, x)):
     *      quo = Poly(quo(matrix[row2, column], matrix[row1, column]), x)
     *      neg_quo = quo * Poly(-1, x)
     *      for i in range(column, rng):
     *          partial = neg_quo * matrix[row1, i]
     *          matrix[row2, i] = partial + matrix[row2, i]
     *
     * @param row1 row1
     * @param row2 row2
     * @param column column
     * @param rng rng
     */
    private void DivideCellsInColumn(int row1, int row2, int column, int rng) throws Exception {
        IMatrix matrix = getHandler().getMatrix();
        if(matrix.get(column, row2) != Polynomial.getZeroPolynomial()) {
            ICommand divideCellsAndReturnCommand = new DivideCellsAndReturnCommand(
                    getHandler(), row2, column, row1, column
            );
            Polynomial quotient = (Polynomial) divideCellsAndReturnCommand.execute();
            getCommands().push(divideCellsAndReturnCommand);

            ICommand negatePolynomialAndReturnCommand = new NegatePolynomialAndReturnCommand(getHandler(), quotient);
            quotient = (Polynomial) negatePolynomialAndReturnCommand.execute();
            getCommands().push(negatePolynomialAndReturnCommand);

            for (int i = column; i < rng; i++) {
                ICommand multiplyPolynomialWithCellAndReturnCommand = new MultiplyPolynomialWithCellAndReturnCommand(getHandler(), quotient, row1, i);
                Polynomial partial = (Polynomial) multiplyPolynomialWithCellAndReturnCommand.execute();
                getCommands().push(multiplyPolynomialWithCellAndReturnCommand);

                ICommand addPolynomialToCellAndSaveCommand = new AddPolynomialToCellAndSaveCommand(getHandler(), partial, row2, i);
                addPolynomialToCellAndSaveCommand.execute();
                getCommands().push(addPolynomialToCellAndSaveCommand);
            }
        }
    }

    /**
     *    if(matrix[row, column2] != Poly(0, x)):
     *       try:
     *           quo = Poly(quo(matrix[row, column2], matrix[row, column1]), x)
     *       except: # matrix[row, column2] = Poly(0, x) and the quo() function is stupid and can't deal with that
     *           quo = Poly(pquo(matrix[row, column2], matrix[row, column1]), x)
     *       neg_quo = quo * Poly(-1, x)
     *       for i in range(row, rng):
     *           partial = neg_quo * matrix[i, column1]
     *           matrix[i, column2] = partial + matrix[i, column2]
     *
     * @param column1 column1
     * @param column2 column2
     * @param row row
     * @param rng rng
     */
    private void processColumns(int column1, int column2, int row, int rng) {

    }

    /**
     *   columnsAllowed = True
     *
     * 	 if(columnsAllowed):
     *      smallest_i, smallest_j = get_smallest(level, rng);
     *   else:
     *      smallest_i, smallest_j = get_smallest_in_column(level, rng);
     *   if(smallest_i > level):
     *      temp = matrix[smallest_i, :]
     *      matrix[smallest_i, :] = matrix[level, :]
     *      matrix[level, :] = temp
     *   if(smallest_j > level and columnsAllowed):
     *      temp = matrix[:, smallest_j]
     *      matrix[:, smallest_j] = matrix[:, level]
     *      matrix[:, level] = temp
     *   return
     *
     * @param lvl lvl
     * @param rng rng
     */
    private void moveSmallestToStart(int lvl, int rng) {

    }

    /**
     * 	cleared = True
     *  for i in range(level + 1, matrix_size):
     *      if(matrix[i, level] != Poly(0, x)):
     *          cleared = False
     *
     * @param level level
     * @return Returns if the column is cleared
     */
    private boolean isColumnCleared(int level) {
        return false;
    }

    /**
     * 	cleared = True
     *  for i in range(level + 1, matrix_size):
     *     if(matrix[level, i] != Poly(0, x)):
     *         cleared = False
     *
     * @param level level
     * @return Returns if the column is cleared
     */
    private boolean isRowCleared(int level) {
        return false;
    }

    /**
     * 	  diagonal_ok = True
     *    last_elem_degree = matrix[0, 0].degree
     *    for i in range(1, matrix_size):
     *        if(matrix[i, i] != Poly(0, x)):
     *            if(matrix[i, i].degree < last_elem_degree):
     *               diagonal_ok = False
     *        last_elem = matrix[i, i].degree
     *
     * @return Returns if the diagonal is cleared
     */
    private boolean isDiagonalOk() {
        return false;
    }

    /**
     * matrix[row1,:] = matrix[row1,:] + matrix[row2,:]
     *
     * @param row1 row1
     * @param row2 row2
     */
    private void addRows(int row1, int row2) {

    }

    /**
     * 	needs_fixing = False
     *  if(matrix[level, level] != Poly(0, x) and matrix[level+1, level+1] != Poly(0, x) and matrix[level, level].degree > matrix[level+1, level+1].degree):
     *      needs_fixing = True
     *
     * @param level level
     * @return Returns if the row needs fixing
     */
    private boolean needsFixing(int level) {
        return false;
    }

    /**
     * 	leading_coefficient = matrix[0, 0].coeff(matrix[0, 0].degree)
     *  if(matrix[0, 0] != Poly(0, x) and leading_coefficient != 1):
     *      for i in range(0, matrix_size):
     *          matrix[0, i] = matrix[0, i] * (1 / leading_coefficient)
     *
     */
    private void fixFirstElement() {

    }
}