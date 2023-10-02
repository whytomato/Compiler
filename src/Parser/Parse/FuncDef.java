package Parser.Parse;

public class FuncDef {
    public FuncType funcType;
    public String ident;
    public FuncFParams funcFParams;
    public Block block;

    public FuncDef() {
        this.funcType = null;
        this.ident = "";
        this.funcFParams = null;
        this.block = null;
    }
}
