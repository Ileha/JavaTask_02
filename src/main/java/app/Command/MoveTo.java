package app.Command;

import app.Singleton.Exception.SinletoneException;
import app.Singleton.SQLiteSingleton;
import org.json.JSONObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoveTo extends ICommand {
    private PreparedStatement check;
    private PreparedStatement is_sub_dir;

    {
        try {
            is_sub_dir = SQLiteSingleton.getInstance().connection.prepareStatement(
                "WITH RECURSIVE cte(c_ref, id) AS (select ref, id from main_table where id = ? UNION all select a.ref, a.id from main_table a inner join cte b on a.id = b.c_ref)select id from cte where id = ?"
            );
            check = SQLiteSingleton.getInstance().connection.prepareStatement(//a - parent b - child
                "SELECT a.type as ptype, b.name, b.type as chtype FROM (select * from main_table where id = ?) a JOIN (select * from main_table where id = ?) b"
            );

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
    public SendingMode OnExecute(JSONObject data, JSONObject out) throws Exception {
        if (data.getInt("p_id") == data.getInt("id")) {
            throw new Exception("you can`t move node to itself");
        }
        is_sub_dir.setInt(1, data.getInt("p_id"));
        is_sub_dir.setInt(2, data.getInt("id"));
        ResultSet result_sub_dir = is_sub_dir.executeQuery();
        if (result_sub_dir.next()) {
            throw new Exception(String.format("parent with id = %s is subdir", data.getInt("p_id")));
        }
        
        check.setObject(1, data.getInt("p_id"));
        check.setObject(2, data.getInt("id"));
        ResultSet resultSet = check.executeQuery();
        if (resultSet.next()) {
            if (!resultSet.getString("ptype").equals("d")) {
                throw new Exception(String.format("it can`t consist node because type = %s", resultSet.getString("type")));
            }
        }
        else {
            throw new Exception(String.format("parent with id = %s not found", data.getInt("p_id")));
        }

        GetStatement().setInt(1, data.getInt("p_id"));
        GetStatement().setInt(2, data.getInt("id"));

        boolean success = GetStatement().executeUpdate() != 0;
        if (success) {
            out.put("cmd", "MoveTo");
            out.put("data", data);
            data.put("name", resultSet.getString("name"));
            data.put("type", resultSet.getString("chtype"));
        }
        return SendingMode.TO_ALL;
    }
}
