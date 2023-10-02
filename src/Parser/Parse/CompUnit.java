package Parser.Parse;

import java.util.ArrayList;

//CompUnit -> {Decl} {FuncDef} MainFuncDef
public class CompUnit {
    public ArrayList<Decl> declArrayList;
    public ArrayList<FuncDef> funcDefArrayList;
    public MainFuncDef mainFuncDef;

    public CompUnit() {
        this.declArrayList = new ArrayList<>();
        this.funcDefArrayList = new ArrayList<>();
        this.mainFuncDef = null;
    }
}

