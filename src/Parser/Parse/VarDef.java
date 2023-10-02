package Parser.Parse;

import java.util.ArrayList;

public class VarDef {
    public String ident;
    public ArrayList<ConstExp> constExpArrayList;
    public InitVal initVal;

    public VarDef() {
        this.ident = "";
        this.constExpArrayList = new ArrayList<>();
        this.initVal = null;
    }
}
