package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.TableRoot;
import symbol.Val;

public class ForStmt {
    public LVal lVal;
    public Exp exp;
    static Lexer lexer = Lexer.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();
    static Error error = Error.getInstance();

    public ForStmt() {
        this.lVal = null;
        this.exp = null;
    }

    public static ForStmt ForStmtParse() {
        ForStmt forStmt = new ForStmt();
        forStmt.lVal = LVal.LValParse();
        Parser.stringBuilder.append("<LVal>\n");
        if (lexer.getType() != LexType.ASSIGN) Parser.error();
        if (tableRoot.search(true, forStmt.lVal.ident)) {
            if (tableRoot.getConst(forStmt.lVal.ident).getType() != -1) {
                Val val = (Val) tableRoot.getConst(forStmt.lVal.ident);
                if (val.getCon() == 1) error.getError('h', lexer.getLine());
            }
        }
        Parser.stringBuilder.append("ASSIGN =\n");
        lexer.next();
        forStmt.exp = Exp.ExpParse();
        Parser.stringBuilder.append("<Exp>\n");
        return forStmt;
    }//ForStmt → LVal '=' Exp
}
