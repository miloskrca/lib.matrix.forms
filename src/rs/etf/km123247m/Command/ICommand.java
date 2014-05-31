package rs.etf.km123247m.Command;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.Command
 */
// TODO: Think these commands through. Should the MatrixForms return a list of commands and then we execute them or we execute them immediately and just store the order.
public interface ICommand {
    public abstract Object execute() throws Exception;
}
