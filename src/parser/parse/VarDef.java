package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.TableRoot;
import symbol.Val;

import java.util.ArrayList;

public class VarDef {
    public String ident;
    public ArrayList<ConstExp> constExpArrayList;
    public InitVal initVal;
    static Lexer lexer = Lexer.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();
    static Error error = Error.getInstance();

    public VarDef() {
        this.ident = "";
        this.constExpArrayList = new ArrayList<>();
        this.initVal = null;
    }

    public static VarDef VarDefParse() {
        int dim = 0;
        boolean flag = true;
        VarDef varDef = new VarDef();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        varDef.ident = lexer.getToken();
        if (tableRoot.search(false, varDef.ident)) {
            flag = false;
            error.getError('b', lexer.getLine());
        }
        Parser.stringBuilder.append("IDENFR ").append(varDef.ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            varDef.constExpArrayList.add(ConstExp.ConstExpParse());
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
            Val val = new Val(tableRoot.getNum(), varDef.ident, 0, dim);
            tableRoot.getSymbolTable().save(val);
        }
        if (lexer.getType() == LexType.ASSIGN) {
            Parser.stringBuilder.append("ASSIGN =\n");
            lexer.next();
            varDef.initVal = InitVal.InitValParse();
            Parser.stringBuilder.append("<InitVal>\n");
        }
        return varDef;
    }//VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
}
