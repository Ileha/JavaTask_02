package app.Singleton;

import app.Command.ICommand;
import app.Singleton.Exception.*;
import java.util.*;

public class CommandBagSingleton {
    private static CommandBagSingleton instance;

    public static void Init(ICommand... cmds) {
        if (instance == null) {
            instance = new CommandBagSingleton();
            for (int i = 0; i < cmds.length; i++) {
                instance.bag.put(cmds[i].GetName(), cmds[i]);
            }
        }

    }
    public static CommandBagSingleton getInstance() throws SinletoneException {
        if (instance == null) {
            throw new NotInitialized("CommandBagSingleton");
        }
        return instance;
    }

    private HashMap<String, ICommand> bag = new HashMap<String, ICommand>();

    public ICommand GetCommand(String name) {
        return bag.get(name);
    }

}
