package Parser.Parse;

import java.util.ArrayList;

public class LVal {
    public String ident;
    public ArrayList<Exp> expArrayList;

    public LVal() {
        this.ident = "";
        this.expArrayList = new ArrayList<>();
    }
}
