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

    //{cmd:"Delete",data:{id=arg1}}
    @Override
    public void OnExecute(JSONObject data, JSONObject out) throws Exception {
        GetStatement().setObject(1, data.getString("id"));
        out.put("status", GetStatement().executeUpdate() != 0);
    }
    //{"res":{status:true/false}}
}
