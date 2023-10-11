package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class UnaryExp {
    public PrimaryExp primaryExp;
    public String ident;
    public FuncRParams funcRParams;
    public UnaryOp unaryOp;
    public UnaryExp unaryExp;
    static Lexer lexer = Lexer.getInstance();

    public UnaryExp() {
        this.primaryExp = null;
        this.ident = "";
        this.funcRParams = null;
        this.unaryOp = null;
        this.unaryExp = null;
    }

    public static UnaryExp UnaryExpParse(LVal lVal) {
        UnaryExp unaryExp = new UnaryExp();
        if (lexer.getType() == LexType.LPARENT) {
            unaryExp.ident = lVal.ident;
            Parser.stringBuilder.append("LPARENT (\n");
            lexer.next();
            if (lexer.getType() != LexType.RPARENT) {
                unaryExp.funcRParams = FuncRParams.FuncRParamsParse();
                Parser.stringBuilder.append("<FuncRParams>\n");
                if (lexer.getType() != LexType.RPARENT) Parser.error();
            }
            Parser.stringBuilder.append("RPARENT )\n");
            lexer.next();
        } else {
            unaryExp.primaryExp = PrimaryExp.PrimaryExpParse(lVal);
            Parser.stringBuilder.append("<PrimaryExp>\n");
        }
        return unaryExp;
    }//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp

    public static UnaryExp UnaryExpParse() {
        UnaryExp unaryExp = new UnaryExp();
        if (lexer.getType() == LexType.LPARENT) {
            unaryExp.primaryExp = PrimaryExp.PrimaryExpParse();
            Parser.stringBuilder.append("<PrimaryExp>\n");
        } else if (lexer.getType() == LexType.IDENFR) {
            if (lexer.getPreType() == LexType.LPARENT) {
                unaryExp.ident = lexer.getToken();
                Parser.stringBuilder.append("IDENFR ").append(unaryExp.ident).append("\n");
                lexer.next();
                if (lexer.getType() != LexType.LPARENT) Parser.error();
                Parser.stringBuilder.append("LPARENT (\n");
                lexer.next();
                if (lexer.getType() != LexType.RPARENT) {
                    unaryExp.funcRParams = FuncRParams.FuncRParamsParse();
                    Parser.stringBuilder.append("<FuncRParams>\n");
                    if (lexer.getType() != LexType.RPARENT) Parser.error();
                }
                Parser.stringBuilder.append("RPARENT )\n");
                lexer.next();
            } else {
                unaryExp.primaryExp = PrimaryExp.PrimaryExpParse();
                Parser.stringBuilder.append("<PrimaryExp>\n");
            }
        } else if (lexer.getType() == LexType.INTCON) {
            unaryExp.primaryExp = PrimaryExp.PrimaryExpParse();
            Parser.stringBuilder.append("<PrimaryExp>\n");
        } else {
            unaryExp.unaryOp = UnaryOp.UnaryOpParse();
            Parser.stringBuilder.append("<UnaryOp>\n");
            unaryExp.unaryExp = UnaryExpParse();
            Parser.stringBuilder.append("<UnaryExp>\n");
        }
        return unaryExp;
    }//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
}
