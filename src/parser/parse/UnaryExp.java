package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.Func;
import symbol.TableRoot;

public class UnaryExp {
    public PrimaryExp primaryExp;
    public String ident;
    public FuncRParams funcRParams;
    public UnaryOp unaryOp;
    public UnaryExp unaryExp;
    public int dim;
    static Lexer lexer = Lexer.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();
    static Error error = Error.getInstance();

    public UnaryExp() {
        this.primaryExp = null;
        this.ident = "";
        this.funcRParams = null;
        this.unaryOp = null;
        this.unaryExp = null;
        this.dim = 0;
    }

    public static UnaryExp UnaryExpParse(LVal lVal) {
        boolean flag = true;
        int num = 0;
        UnaryExp unaryExp = new UnaryExp();
        Func func = null;
        if (lexer.getType() == LexType.LPARENT) {
            unaryExp.ident = lVal.ident;
            Parser.stringBuilder.append("LPARENT (\n");
            if (tableRoot.getSymbol(unaryExp.ident).getType() == -1) {
                func = (Func) tableRoot.getSymbol(unaryExp.ident);
            }
            lexer.next();
            if (lexer.getType() != LexType.RPARENT) {
                unaryExp.funcRParams = FuncRParams.FuncRParamsParse(unaryExp.ident, lVal.line);
                Parser.stringBuilder.append("<FuncRParams>\n");
                if (lexer.getType() != LexType.RPARENT) {
                    flag = false;
                    error.getError('j', lexer.getLastLine());
                }
                num = unaryExp.funcRParams.num;
            }
            if (flag) {
                if (func != null && func.getParamTypeList().size() != num) {
                    error.getError('d', lVal.line);
                }
                Parser.stringBuilder.append("RPARENT )\n");
                lexer.next();
            }
        } else {
            unaryExp.primaryExp = PrimaryExp.PrimaryExpParse(lVal);
            Parser.stringBuilder.append("<PrimaryExp>\n");
        }
        return unaryExp;
    }//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp

    public static UnaryExp UnaryExpParse() {
        boolean flag = true;
        int num = 0;
        UnaryExp unaryExp = new UnaryExp();
        if (lexer.getType() == LexType.LPARENT) {
            unaryExp.primaryExp = PrimaryExp.PrimaryExpParse();
            unaryExp.dim = unaryExp.primaryExp.dim;
            Parser.stringBuilder.append("<PrimaryExp>\n");
        } else if (lexer.getType() == LexType.IDENFR) {
            int line = lexer.getLine();
            Func func = null;
            if (lexer.getPreType() == LexType.LPARENT) {
                unaryExp.ident = lexer.getToken();
                if (tableRoot.getSymbol(unaryExp.ident) != null) {
                    if (tableRoot.getSymbol(unaryExp.ident).getType() != -1)
                        error.getError('c', lexer.getLine());
                    else {
                        func = (Func) tableRoot.getSymbol(unaryExp.ident);
                    }
                } else error.getError('c', lexer.getLine());
                Parser.stringBuilder.append("IDENFR ").append(unaryExp.ident).append("\n");
                lexer.next();
                if (lexer.getType() != LexType.LPARENT) Parser.error();
                Parser.stringBuilder.append("LPARENT (\n");
                lexer.next();
                if (lexer.getType() != LexType.RPARENT) {
                    unaryExp.funcRParams = FuncRParams.FuncRParamsParse(unaryExp.ident, line);
                    Parser.stringBuilder.append("<FuncRParams>\n");
                    if (lexer.getType() != LexType.RPARENT) {
                        flag = false;
                        error.getError('j', lexer.getLastLine());
                    }
                    num = unaryExp.funcRParams.num;
                }
                if (flag) {
                    if (func != null && func.getParamTypeList().size() != num)
                        error.getError('d', line);
                    Parser.stringBuilder.append("RPARENT )\n");
                    if (func != null) {
                        if (func.getRetype() == 0) unaryExp.dim = 0;
                        else unaryExp.dim = -1;
                    }
                    lexer.next();
                }
            } else {
                unaryExp.primaryExp = PrimaryExp.PrimaryExpParse();
                unaryExp.dim = unaryExp.primaryExp.dim;
                Parser.stringBuilder.append("<PrimaryExp>\n");
            }
        } else if (lexer.getType() == LexType.INTCON) {
            unaryExp.primaryExp = PrimaryExp.PrimaryExpParse();
            unaryExp.dim = 0;
            Parser.stringBuilder.append("<PrimaryExp>\n");
        } else if (lexer.getType() == LexType.PLUS || lexer.getType() == LexType.MINU || lexer.getType() == LexType.NOT) {
            unaryExp.unaryOp = UnaryOp.UnaryOpParse();
            Parser.stringBuilder.append("<UnaryOp>\n");
            unaryExp.unaryExp = UnaryExpParse();
            unaryExp.dim = unaryExp.unaryExp.dim;
            Parser.stringBuilder.append("<UnaryExp>\n");
        }
        return unaryExp;
    }//UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
}
