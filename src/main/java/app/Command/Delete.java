package app.Command;

import app.Singleton.SQLiteSingleton;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.PreparedStatement;

public class Delete extends ICommand {

    @Override
    protected String PreparedRequest() {
        return "DELETE FROM main_table WHERE id=?";
    }
    //delete from main_table where id=2

    @Override
    public String GetName() {
        return "Delete";
    }

    @Override
    protected SendingMode OnExecute(JSONObject data, JSONObject out) throws Exception {
        GetStatement().setObject(1, data.getString("id"));
        boolean success = GetStatement().executeUpdate() != 0;
        if (success) {
            out.put("cmd", "Delete");
            out.put("data", data);
        }
        return SendingMode.TO_ALL;
    }

    //{cmd:"Delete",data:{id=arg1}}
    //{cmd:"Delete",data:{id=arg1}}
}
