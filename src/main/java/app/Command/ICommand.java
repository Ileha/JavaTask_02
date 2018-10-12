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

    //input: {cmd:"cmd_name", data:{data}}
    //output: {cmd:"cmd_name", data:{data}}

    /*available:

        {cmd:"Delete",data:{id}}
        {cmd:"GetByParent",data:{p_id}}
        {cmd:"MoveTo",data:{id, p_id}}
        {cmd:"Rename",data:{name, id}}
        {cmd:"InsertContainer", data:{p_id,name}}
        {cmd:"InsertEnd", data:{p_id,name}}

    */

    protected abstract SendingMode OnExecute(JSONObject data, JSONObject out) throws Exception;
    public final SendingMode Execute(JSONObject data, StringBuilder out) throws Exception {
        JSONObject json_out = new JSONObject();
        SendingMode res;
        synchronized (statement) {
            res = OnExecute(data, json_out);
        }
        out.append(json_out.toString());
        return res;
    }

    protected PreparedStatement GetStatement() {
        return statement;
    }
}
