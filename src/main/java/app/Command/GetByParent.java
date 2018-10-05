package app.Command;

import org.json.*;
import java.io.PrintWriter;
import java.sql.*;

public class GetByParent extends ICommand {

    @Override
    protected String PreparedRequest() {
        return "SELECT id, name, type FROM main_table where ref=?";
    }

    @Override
    public String GetName() {
        return "GetByParent";
    }

    //{cmd:"GetByParent",data:{p_id=arg1}}
    @Override
    public void OnExecute(JSONObject data, JSONObject out) throws Exception {
        GetStatement().setObject(1, data.getInt("p_id"));
        ResultSet resultSet = GetStatement().executeQuery();
        JSONArray array = new JSONArray();
        while (resultSet.next()) {
//            out.printf("id = %s, name = %s, type = %s</br>", resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("type"));
            JSONObject obj = new JSONObject();
            obj.put("id", resultSet.getInt("id"));
            obj.put("name", resultSet.getString("name"));
            obj.put("type", resultSet.getString("type"));
            array.put(obj);
        }
        out.put("array", array);
        //[{},{}]
    }
}
