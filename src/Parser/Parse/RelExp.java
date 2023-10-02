package Parser.Parse;

import java.util.ArrayList;

public class RelExp {
    public ArrayList<AddExp> addExpArrayList;
    public ArrayList<String> RelOp;

    public RelExp() {
        this.addExpArrayList = new ArrayList<>();
        this.RelOp = new ArrayList<>();
    }
}
