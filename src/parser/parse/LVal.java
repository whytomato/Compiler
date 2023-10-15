package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.TableRoot;
import symbol.Val;

import java.util.ArrayList;

public class LVal {
    public String ident;
    public ArrayList<Exp> expArrayList;
    public int Dim;
    public int line;
    static Lexer lexer = Lexer.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();
    static Error error = Error.getInstance();

    public LVal() {
        this.line = 0;
        this.ident = "";
        this.expArrayList = new ArrayList<>();
        this.Dim = 0;
    }

    public static LVal LValParse() {
        int dim = 0;
        LVal lVal = new LVal();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        lVal.ident = lexer.getToken();
        lVal.line = lexer.getLine();
        Parser.stringBuilder.append("IDENFR ").append(lVal.ident).append("\n");
        if (tableRoot.search(true, lVal.ident)) {
            if (tableRoot.getConst(lVal.ident).getType() != -1) {
                Val val = (Val) tableRoot.getConst(lVal.ident);
                dim = val.getType();
            } else dim = tableRoot.getConst(lVal.ident).getType();
        }
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            lVal.expArrayList.add(Exp.ExpParse());
            Parser.stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.RBRACK)
                error.getError('k', lexer.getLastLine());
            else {
                Parser.stringBuilder.append("RBRACK ]\n");
                lexer.next();
                dim--;
            }
        }
        if (!tableRoot.search(true, lVal.ident)) {
            error.getError('c', lexer.getLine());
        }
        lVal.Dim = dim;
        return lVal;
    }//LVal → Ident {'[' Exp ']'}
}
