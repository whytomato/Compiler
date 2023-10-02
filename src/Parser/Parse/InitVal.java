package Parser.Parse;

import java.util.ArrayList;

public class InitVal {
    public Exp exp;
    public ArrayList<InitVal> initValArrayList;

    public InitVal() {
        this.exp = null;
        this.initValArrayList = new ArrayList<>();
    }
}
