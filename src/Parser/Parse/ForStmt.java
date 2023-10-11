package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class ForStmt {
    public LVal lVal;
    public Exp exp;
    static Lexer lexer = Lexer.getInstance();

    public ForStmt() {
        this.lVal = null;
        this.exp = null;
    }

    public static ForStmt ForStmtParse() {
        ForStmt forStmt = new ForStmt();
        forStmt.lVal = LVal.LValParse();
        Parser.stringBuilder.append("<LVal>\n");
        if (lexer.getType() != LexType.ASSIGN) Parser.error();
        Parser.stringBuilder.append("ASSIGN =\n");
        lexer.next();
        forStmt.exp = Exp.ExpParse();
        Parser.stringBuilder.append("<Exp>\n");
        return forStmt;
    }//ForStmt → LVal '=' Exp
}
