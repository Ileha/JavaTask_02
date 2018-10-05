package app.Command;

import org.json.JSONObject;
import java.io.PrintWriter;

public class InsertContainer extends IInsert {
    @Override
    public String GetName() {
        return "InsertContainer";
    }

    @Override
    protected String MeanType() {
        return "d";
    }
}
