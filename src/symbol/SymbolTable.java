package symbol;

import java.util.HashMap;

public class SymbolTable {
    private int id;
    private int fatherId;
    private String fatherName;
    private HashMap<String, Symbol> directory = new HashMap<>();

    public SymbolTable(int num, String fatherName) {
        this.id = num;
        this.fatherId = (num - 1);
        this.fatherName = fatherName;
    }

    public SymbolTable(int num) {
        this.id = num;
        this.fatherId = (num - 1);
    }

    public void save(Symbol symbol) {
        directory.put(symbol.getToken(), symbol);
    }

    public int getId() {
        return id;
    }

    public int getFatherId() {
        return fatherId;
    }

    public String getFatherName() {
        return fatherName;
    }

    //    public boolean search(String token) {
//        if (directory.containsKey(token)) return false;
//    }
    public HashMap<String, Symbol> getDirectory() {
        return directory;
    }

    public void printf() {
        for (Symbol key : directory.values()) {
            if (key instanceof Val val) {
                System.out.println(val.getToken() + " " + val.getType() + " " + val.getCon());
            }
            if (key instanceof Func func) {
                System.out.println(func.getToken() + " " + func.getType() + " " + func.getRetype() + " " + func.getParamTypeList());
            }
        }
    }
}
