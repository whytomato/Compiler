package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.TableRoot;
import symbol.Val;

import java.util.ArrayList;

public class FuncFParam {
    public String ident;
    public int dim;
    public ArrayList<ConstExp> constExpArrayList;
    static Lexer lexer = Lexer.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();
    static Error error = Error.getInstance();

    public FuncFParam() {
        this.ident = "";
        this.dim = 0;
        this.constExpArrayList = new ArrayList<>();
    }

    public static FuncFParam FuncFParamParse() {
        boolean flag = true;
        FuncFParam funcFParam = new FuncFParam();
        if (lexer.getType() != LexType.INTTK) Parser.error();
        Parser.stringBuilder.append("INTTK int\n");
        lexer.next();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        funcFParam.ident = lexer.getToken();
        if (tableRoot.search(false, funcFParam.ident)) {
            flag = false;
            error.getError('b', lexer.getLine());
        }
        Parser.stringBuilder.append("IDENFR ").append(funcFParam.ident).append("\n");
        lexer.next();
        if (lexer.getType() == LexType.LBRACK) {
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            if (lexer.getType() != LexType.RBRACK) {
                error.getError('k', lexer.getLastLine());
            } else {
                Parser.stringBuilder.append("RBRACK ]\n");
                funcFParam.dim++;
                lexer.next();
            }
            while (true) {
                if (lexer.getType() != LexType.LBRACK) break;
                Parser.stringBuilder.append("LBRACK [\n");
                lexer.next();
                funcFParam.constExpArrayList.add(ConstExp.ConstExpParse());
                Parser.stringBuilder.append("<ConstExp>\n");
                if (lexer.getType() != LexType.RBRACK) {
                    flag = false;
                    error.getError('k', lexer.getLastLine());
                } else {
                    Parser.stringBuilder.append("RBRACK ]\n");
                    lexer.next();
                    funcFParam.dim++;
                }
            }
        }
        if (flag) {
            Val val = new Val(tableRoot.getNum(), funcFParam.ident, 0, funcFParam.dim);
            tableRoot.getSymbolTable().save(val);
        }
        return funcFParam;
    }//FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]

}
