package Parser.Parse;

import java.util.ArrayList;

public class Stmt {
    public LVal lVal;
    public ArrayList<Exp> expArrayList;
    public Block block;
    public Cond cond;
    public ArrayList<Stmt> stmtArrayList;
    public ArrayList<ForStmt> forStmtArrayList;
    public String formatString;
    public int flag;

    public Stmt() {
        this.lVal = null;
        this.expArrayList = new ArrayList<>();
        this.block = null;
        this.cond = null;
        this.stmtArrayList = new ArrayList<>();
        this.forStmtArrayList = new ArrayList<>();
        this.formatString = "";
        this.flag = 0;
    }
}
