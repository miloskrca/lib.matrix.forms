package rs.etf.km123247m.Command;

import rs.etf.km123247m.Matrix.Handler.MatrixHandler;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command
 */
public interface ICommand {
    public abstract Object execute(MatrixHandler handler) throws Exception;
}
