package app.Command;

import app.Singleton.Exception.SinletoneException;
import app.Singleton.SQLiteSingleton;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoveTo extends ICommand {
    private PreparedStatement check;

    {
        try {
            check = SQLiteSingleton.getInstance().connection.prepareStatement(
                    "SELECT type FROM main_table WHERE ref=?");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SinletoneException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String PreparedRequest() {
        return "UPDATE main_table SET ref=? WHERE id=?";
    }

    @Override
    public String GetName() {
        return "MoveTo";
    }

    //{cmd:"MoveTo",data:{id=arg1, p_id=arg2}}
    @Override
    public void OnExecute(JSONObject data, JSONObject out) throws Exception {
        if (data.getInt("p_id") == data.getInt("id")) {
            throw new Exception("you can`t move node to itself");
        }
        check.setObject(1, data.getInt("p_id"));
        ResultSet resultSet = check.executeQuery();
        if (!resultSet.getString("type").equals("d")) {
            throw new Exception(String.format("it can`t consist node because type = %s", resultSet.getString("type")));
        }

        GetStatement().setInt(1, data.getInt("p_id"));
        GetStatement().setInt(2, data.getInt("id"));
        out.put("status", GetStatement().executeUpdate() != 0);
    }
}
