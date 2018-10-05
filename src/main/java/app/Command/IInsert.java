package app.Command;

import app.Singleton.Exception.SinletoneException;
import app.Singleton.SQLiteSingleton;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class IInsert extends ICommand {
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

    protected abstract String MeanType();

    @Override
    protected final String PreparedRequest() {
        return "INSERT INTO main_table (`name`, `type`, `ref`) VALUES(?, ?, ?)";
    }

    @Override
    public final void OnExecute(JSONObject data, JSONObject out) throws Exception {
        check.setObject(1, data.getString("p_id"));
        ResultSet resultSet = check.executeQuery();
        if (!resultSet.getString("type").equals("d")) {
            throw new Exception("it can`t consist node");
        }
        GetStatement().setObject(1, data.getString("name"));
        GetStatement().setObject(2, MeanType());
        GetStatement().setObject(3, data.getInt("p_id"));
        out.put("status", GetStatement().executeUpdate() != 0);
    }
}
