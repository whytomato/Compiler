package Parser.Parse;

import java.util.ArrayList;

public class ConstDef {
    public String Ident;
    public ArrayList<ConstExp> constExpArrayList;
    public ConstInitval constInitval;

    public ConstDef() {
        this.Ident = "";
        this.constExpArrayList = new ArrayList<>();
        this.constInitval = null;
    }
}
