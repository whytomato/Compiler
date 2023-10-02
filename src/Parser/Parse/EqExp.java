package Parser.Parse;

import java.util.ArrayList;

public class EqExp {
    public ArrayList<RelExp> relExpArrayList;
    public ArrayList<String> eqOp;

    public EqExp() {
        this.relExpArrayList = new ArrayList<>();
        this.eqOp = new ArrayList<>();
    }

}
