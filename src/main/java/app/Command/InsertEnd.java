package app.Command;

public class InsertEnd extends IInsert {

    //{cmd:"InsertEnd",data:{name="name", p_id=arg1}}
    @Override
    public String GetName() {
        return "InsertEnd";
    }

    @Override
    protected String MeanType() {
        return "f";
    }
}
