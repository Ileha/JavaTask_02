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
    public SendingMode OnExecute(JSONObject data, JSONObject out) throws Exception {
        int parent_id = data.getInt("p_id");
        GetStatement().setObject(1, parent_id);
        ResultSet resultSet = GetStatement().executeQuery();
        JSONArray array = new JSONArray();
        while (resultSet.next()) {
            JSONObject obj = new JSONObject();
            obj.put("id", resultSet.getInt("id"));
            obj.put("name", resultSet.getString("name"));
            obj.put("type", resultSet.getString("type"));
            array.put(obj);
        }
        JSONObject data_out = new JSONObject();
        data_out.put("array", array);
        data_out.put("parent", parent_id);
        out.put("data", data_out);
        out.put("cmd", "SetChild");
        return SendingMode.TO_SENDER;
    }
}
