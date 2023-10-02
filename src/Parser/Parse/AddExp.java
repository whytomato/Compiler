package Parser.Parse;

import java.util.ArrayList;

public class AddExp {
    public ArrayList<MulExp> mulExpArrayList;
    public ArrayList<String> addOp;

    public AddExp() {
        this.mulExpArrayList = new ArrayList<>();
        this.addOp = new ArrayList<>();
    }
}
