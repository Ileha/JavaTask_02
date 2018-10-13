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
                    "SELECT type FROM main_table WHERE id=?");
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

    //{cmd:"InsertContainer", data:{p_id,name}}
    //{cmd:"InsertEnd", data:{p_id,name}}
    @Override
    public final SendingMode OnExecute(JSONObject data, JSONObject out) throws Exception {
        if (data.getString("name").equals("")) {
            throw new Exception("name can`t be empty");
        }
        
        check.setInt(1, data.getInt("p_id"));
        ResultSet resultSet = check.executeQuery();
        if (resultSet.next()) {
            if (!resultSet.getString("type").equals("d")) {
                throw new Exception(String.format("it can`t consist node because it is %s", resultSet.getString("type")));
            }
        }
        else {
            throw new Exception(String.format("parent with id = %s not found", data.getInt("p_id")));
        }
        GetStatement().setObject(1, data.getString("name"));
        GetStatement().setObject(2, MeanType());
        GetStatement().setObject(3, data.getInt("p_id"));
        boolean success = GetStatement().executeUpdate() != 0;
        if (success) {
            out.put("cmd", GetName());
            out.put("data", data);
            try (ResultSet generatedKeys = GetStatement().getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    data.put("new_id", generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
        return SendingMode.TO_ALL;
    }
}
