package rs.etf.km123247m.Matrix.Forms.Implementation;

import rs.etf.km123247m.Matrix.Forms.MatrixForm;
import rs.etf.km123247m.Matrix.Handler.Implementation.SymJaMatrixHandler;
import rs.etf.km123247m.Matrix.Handler.MatrixHandler;
import rs.etf.km123247m.Observers.Event.FormEvent;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 *
 * package: rs.etf.km123247m.Matrix.Forms.Implementation
 */
// TODO: implement Smiths form first. Work on IMatrixForm after you see what is actually needed.
public class SmithMatrixForm extends MatrixForm {

    public SmithMatrixForm(MatrixHandler handler) {
        super(handler);
    }

    @Override
    public void process() {
        sendUpdate(FormEvent.PROCESSING_STATUS);

//        for (int subMatrixLvl = 0; subMatrixLvl < handler.getSize() - 1; subMatrixLvl++) {
//
//            MainFrame.writeLeft("Started proccessing on level " + subMatrixLvl);
//            MainFrame.writeLeft("=============================");
//            do {
//
//                if (!work)
//                    return;
//
//                do {
//
//                    handler.moveSmallestToStart(subMatrixLvl, handler.getSize());
//                    MainFrame.writeLeft("Moving smallest to start...");
//                    MainFrame.writeLeft("======================");
//                    MainFrame.writeLeft(MatrixFormater.getFormatedMatrix(
//                            handler.toString(), handler.getSize()));
//                    MainFrame.writeLeft("======================");
//
//                    for (int row2 = subMatrixLvl + 1; row2 < handler.getSize(); row2++) {
//                        handler.processRows(subMatrixLvl, row2, subMatrixLvl,
//                                handler.getSize());
//                        MainFrame.writeLeft(MatrixFormater.getFormatedMatrix(
//                                handler.toString(), handler.getSize()));
//                    }
//
//                } while (!handler.isColumnCleared(subMatrixLvl) && work);
//
//                MainFrame.writeLeft("======================");
//                MainFrame.writeLeft("finished with column");
//                MainFrame.writeLeft("======================");
//
//                if (!work)
//                    return;
//
//                for (int column2 = subMatrixLvl + 1; column2 < handler
//                        .getSize(); column2++) {
//                    handler.processColumns(subMatrixLvl, column2, subMatrixLvl,
//                            handler.getSize());
//                    MainFrame.writeLeft(MatrixFormater.getFormatedMatrix(
//                            handler.toString(), handler.getSize()));
//                }
//
//                MainFrame.writeLeft("======================");
//                MainFrame.writeLeft("finished with row");
//                MainFrame.writeLeft("======================");
//
//            } while (!handler.isRowCleared(subMatrixLvl) && work);
//
//            MainFrame.writeLeft("Done with the level " + subMatrixLvl);
//            MainFrame.writeLeft("=============================");
//            // }
//        }


//        if (!work)
//            return;
//
//        MainFrame.writeLeft("Checking elements on the diagonal...");
//        MainFrame.writeLeft("=============================");
//
//        if (!handler.isDiagonalOk()) {
//
//            MainFrame.writeLeft("=============================");
//            MainFrame.writeLeft("Diagonal not ok. Fixing.");
//            MainFrame.writeLeft("=============================");
//
//            for (int row = 0; row < handler.getSize() - 1; row++) {
//
//                if (handler.needsFixing(row)) {
//
//                    int column = row;
//
//                    handler.addRows(row, row + 1);
//                    MainFrame.writeLeft("Adding row " + (row + 1) + " to "
//                            + row);
//                    MainFrame.writeLeft(MatrixFormater.getFormatedMatrix(
//                            handler.toString(), handler.getSize()));
//
//                    do {
//                        do {
//
//                            MainFrame.writeLeft(handler.getSmallest(row,
//                                    row + 2));
//                            handler.moveSmallestToStart(row, row + 2);
//                            MainFrame.writeLeft("moveSmallestToStart");
//                            MainFrame.writeLeft(MatrixFormater
//                                    .getFormatedMatrix(handler.toString(),
//                                            handler.getSize()));
//
//                            handler.processColumns(column, column + 1, row,
//                                    row + 2);
//                            MainFrame.writeLeft("processColumns = row "
//                                    + column);
//                            MainFrame.writeLeft(MatrixFormater
//                                    .getFormatedMatrix(handler.toString(),
//                                            handler.getSize()));
//
//                        } while (!handler.isRowCleared(row) && work);
//
//                        handler.processRows(row, row + 1, row, row + 2);
//                        MainFrame.writeLeft("processRows = column  " + row);
//                        MainFrame.writeLeft(MatrixFormater.getFormatedMatrix(
//                                handler.toString(), handler.getSize()));
//
//                    } while (!handler.isColumnCleared(column) && work);
//
//                }
//
//            }
//
//        } else {
//            MainFrame.writeLeft("=============================");
//            MainFrame.writeLeft("Diagonal ok.");
//            MainFrame.writeLeft("=============================");
//        }
//
//        if (!work)
//            return;
//
//        MainFrame.writeLeft("=============================");
//        MainFrame.writeLeft("Fix element on position [0, 0] if needed...");
//        MainFrame.writeLeft("=============================");
//
//        String lc = handler.fixFirstElement();
//        MainFrame.writeLeft("Leading coefficient is: " + lc);
//        MainFrame.writeLeft(MatrixFormater.getFormatedMatrix(
//                handler.toString(), handler.getSize()));
//
//        MainFrame.writeLeft("Finished:");
//        MainFrame.writeLeft("===================================");
//        MainFrame.writeRight("Finished:");
//        MainFrame.writeRight("===================================");


    }
}