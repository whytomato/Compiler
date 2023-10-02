package Parser.Parse;

import java.util.ArrayList;

public class FuncFParam {
    public String ident;
    public ArrayList<ConstExp> constExpArrayList;

    public FuncFParam() {
        this.ident = "";
        this.constExpArrayList = new ArrayList<>();
    }
}
