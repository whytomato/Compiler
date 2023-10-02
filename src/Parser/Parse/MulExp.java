package Parser.Parse;

import java.util.ArrayList;

public class MulExp {
    public ArrayList<UnaryExp> unaryExpArrayList;
    public ArrayList<String> mulOp;

    public MulExp() {
        this.unaryExpArrayList = new ArrayList<>();
        this.mulOp = new ArrayList<>();
    }
}
