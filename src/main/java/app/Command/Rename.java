package app.Command;

import app.Singleton.Exception.SinletoneException;
import app.Singleton.SQLiteSingleton;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Rename extends ICommand {
    @Override
    protected String PreparedRequest() {
        return "UPDATE main_table SET name=? WHERE id=?";
    }

    @Override
    public String GetName() {
        return "Rename";
    }

    //{cmd:"Rename",data:{name="name", id=arg1}}
    @Override
    public void OnExecute(JSONObject data, JSONObject out) throws Exception {
        GetStatement().setString(1, data.getString("name"));
        GetStatement().setInt(2, data.getInt("id"));
        out.put("status", GetStatement().executeUpdate() != 0);
    }
}
