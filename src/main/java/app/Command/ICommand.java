package app.Command;

import app.Singleton.Exception.SinletoneException;
import app.Singleton.SQLiteSingleton;
import org.json.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ICommand {
    private PreparedStatement statement;

    {
        try {
            statement = SQLiteSingleton.getInstance().connection.prepareStatement(PreparedRequest());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SinletoneException e) {
            e.printStackTrace();
        }
    }

    protected abstract String PreparedRequest();
    public abstract String GetName();
    protected abstract void OnExecute(JSONObject data, JSONObject out) throws Exception;
    public final void Execute(JSONObject data, PrintWriter out) throws Exception {
        JSONObject json_out = new JSONObject();
        JSONObject res = new JSONObject();
        json_out.put("res", res);
        OnExecute(data, res);
        out.println(json_out.toString());
    }

    protected PreparedStatement GetStatement() {
        return statement;
    }
}
