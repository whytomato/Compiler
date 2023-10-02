package Parser.Parse;

import java.util.ArrayList;

public class ConstInitval {
    public ConstExp constExp;
    public ArrayList<ConstInitval> constInitvalArrayList;

    public ConstInitval() {
        this.constExp = null;
        this.constInitvalArrayList = new ArrayList<>();
    }
}
