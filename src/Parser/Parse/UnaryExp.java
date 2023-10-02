package Parser.Parse;

public class UnaryExp {
    public PrimaryExp primaryExp;
    public String ident;
    public FuncRParams funcRParams;
    public UnaryOp unaryOp;
    public UnaryExp unaryExp;

    public UnaryExp() {
        this.primaryExp = null;
        this.ident = "";
        this.funcRParams = null;
        this.unaryOp = null;
        this.unaryExp = null;
    }
}
