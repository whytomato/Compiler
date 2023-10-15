package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.TableRoot;
import symbol.Val;

import java.util.ArrayList;

public class ConstDef {
    public String Ident;
    public ArrayList<ConstExp> constExpArrayList;
    public ConstInitVal constInitval;
    static Lexer lexer = Lexer.getInstance();
    static Error error = Error.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();

    public ConstDef() {
        this.Ident = "";
        this.constExpArrayList = new ArrayList<>();
        this.constInitval = null;
    }

    public static ConstDef ConstDefParse() {
        int dim = 0;
        boolean flag = true;
        ConstDef constDef = new ConstDef();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        constDef.Ident = lexer.getToken();
        if (tableRoot.search(false, constDef.Ident)) {
            flag = false;
            error.getError('b', lexer.getLine());//同层
        }
        Parser.stringBuilder.append("IDENFR ").append(constDef.Ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            constDef.constExpArrayList.add(ConstExp.ConstExpParse());
            Parser.stringBuilder.append("<ConstExp>\n");
            if (lexer.getType() != LexType.RBRACK) {
//                flag = false;
                error.getError('k', lexer.getLastLine());
            } else {
                Parser.stringBuilder.append("RBRACK ]\n");
                lexer.next();
            }
            dim++;
        }
        if (flag) {
            Val val = new Val(tableRoot.getNum(), constDef.Ident, 1, dim);
            tableRoot.getSymbolTable().save(val);
        }
        if (lexer.getType() != LexType.ASSIGN) Parser.error();
        Parser.stringBuilder.append("ASSIGN =\n");
        lexer.next();
        constDef.constInitval = ConstInitVal.ConstInitValParse();
        Parser.stringBuilder.append("<ConstInitVal>\n");
        return constDef;
    }//ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
}
