package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class Stmt {
    public LVal lVal;
    public ArrayList<Exp> expArrayList;
    public Block block;
    public Cond cond;
    public ArrayList<Stmt> stmtArrayList;
    public ArrayList<ForStmt> forStmtArrayList;
    public String formatString;
    public int flag;
    static Lexer lexer = Lexer.getInstance();

    public Stmt() {
        this.lVal = null;
        this.expArrayList = new ArrayList<>();
        this.block = null;
        this.cond = null;
        this.stmtArrayList = new ArrayList<>();
        this.forStmtArrayList = new ArrayList<>();
        this.formatString = "";
        this.flag = 0;
    }

    public static Stmt StmtParse() {
        Stmt stmt = new Stmt();
        if (lexer.getType() == LexType.LBRACE) {
            stmt.flag = 3;
            stmt.block = Block.BlockParse();
            Parser.stringBuilder.append("<Block>\n");
        } else if (lexer.getType() == LexType.IFTK) {
            Parser.stringBuilder.append("IFTK if\n");
            lexer.next();
            if (lexer.getType() != LexType.LPARENT) Parser.error();
            Parser.stringBuilder.append("LPARENT (\n");
            lexer.next();
            stmt.cond = Cond.CondParse();
            Parser.stringBuilder.append("<Cond>\n");
            if (lexer.getType() != LexType.RPARENT) Parser.error();
            Parser.stringBuilder.append("RPARENT )\n");
            lexer.next();
            stmt.stmtArrayList.add(StmtParse());
            Parser.stringBuilder.append("<Stmt>\n");
            if (lexer.getType() == LexType.ELSETK) {
                Parser.stringBuilder.append("ELSETK else\n");
                lexer.next();
                stmt.flag = 4;
                stmt.stmtArrayList.add(StmtParse());
                Parser.stringBuilder.append("<Stmt>\n");
            }
        } else if (lexer.getType() == LexType.FORTK) {
            Parser.stringBuilder.append("FORTK for\n");
            lexer.next();
            if (lexer.getType() != LexType.LPARENT) Parser.error();
            Parser.stringBuilder.append("LPARENT (\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) {
                stmt.forStmtArrayList.add(ForStmt.ForStmtParse());
                Parser.stringBuilder.append("<ForStmt>\n");
                if (lexer.getType() != LexType.SEMICN) Parser.error();
            }
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) {
                stmt.cond = Cond.CondParse();
                Parser.stringBuilder.append("<Cond>\n");
                if (lexer.getType() != LexType.SEMICN) Parser.error();
            }
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            if (lexer.getType() != LexType.RPARENT) {
                stmt.forStmtArrayList.add(ForStmt.ForStmtParse());
                Parser.stringBuilder.append("<ForStmt>\n");
                if (lexer.getType() != LexType.RPARENT) Parser.error();
            }
            Parser.stringBuilder.append("RPARENT )\n");
            lexer.next();
            stmt.flag = 5;
            stmt.stmtArrayList.add(StmtParse());
            Parser.stringBuilder.append("<Stmt>\n");

        } else if (lexer.getType() == LexType.BREAKTK) {
            Parser.stringBuilder.append("BREAKTK break\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) Parser.error();
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 6;
        } else if (lexer.getType() == LexType.CONTINUETK) {
            Parser.stringBuilder.append("CONTINUETK continue\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) Parser.error();
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 7;
        } else if (lexer.getType() == LexType.RETURNTK) {
            Parser.stringBuilder.append("RETURNTK return\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) {
                stmt.expArrayList.add(Exp.ExpParse());
                Parser.stringBuilder.append("<Exp>\n");
                if (lexer.getType() != LexType.SEMICN) Parser.error();
            }
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 8;
        } else if (lexer.getType() == LexType.PRINTFTK) {
            Parser.stringBuilder.append("PRINTFTK printf\n");
            lexer.next();
            if (lexer.getType() != LexType.LPARENT) Parser.error();
            Parser.stringBuilder.append("LPARENT (\n");
            lexer.next();
            if (lexer.getType() != LexType.STRCON) Parser.error();
            stmt.formatString = lexer.getToken();
            Parser.stringBuilder.append("STRCON ").append(stmt.formatString).append("\n");
            lexer.next();
            while (true) {
                if (lexer.getType() != LexType.COMMA) break;
                Parser.stringBuilder.append("COMMA ,\n");
                lexer.next();
                stmt.expArrayList.add(Exp.ExpParse());
                Parser.stringBuilder.append("<Exp>\n");
            }
            if (lexer.getType() != LexType.RPARENT) Parser.error();
            Parser.stringBuilder.append("RPARENT )\n");
            lexer.next();
            if (lexer.getType() != LexType.SEMICN) Parser.error();
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 10;
        } else if (lexer.getType() == LexType.IDENFR) {
            stmt.lVal = LVal.LValParse();
            if (lexer.getType() != LexType.ASSIGN) {
                stmt.expArrayList.add(Exp.ExpParse(stmt.lVal));
                Parser.stringBuilder.append("<Exp>\n");
                if (lexer.getType() != LexType.SEMICN) Parser.error();
                Parser.stringBuilder.append("SEMICN ;\n");
                lexer.next();
                stmt.flag = 2;
                return stmt;
            }
            Parser.stringBuilder.append("<LVal>\n");
            Parser.stringBuilder.append("ASSIGN =\n");
            lexer.next();
            if (lexer.getType() == LexType.GETINTTK) {
                Parser.stringBuilder.append("GETINTTK getint\n");
                lexer.next();
                if (lexer.getType() != LexType.LPARENT) Parser.error();
                Parser.stringBuilder.append("LPARENT (\n");
                lexer.next();
                if (lexer.getType() != LexType.RPARENT) Parser.error();
                Parser.stringBuilder.append("RPARENT )\n");
                lexer.next();
                if (lexer.getType() != LexType.SEMICN) Parser.error();
                Parser.stringBuilder.append("SEMICN ;\n");
                lexer.next();
                stmt.flag = 9;
            } else {
                stmt.expArrayList.add(Exp.ExpParse());
                Parser.stringBuilder.append("<Exp>\n");
                if (lexer.getType() != LexType.SEMICN) Parser.error();
                Parser.stringBuilder.append("SEMICN ;\n");
                lexer.next();
                stmt.flag = 1;
            }

        } else if (lexer.getType() == LexType.SEMICN) {
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 2;
        } else {
            stmt.expArrayList.add(Exp.ExpParse());
            Parser.stringBuilder.append("<Exp>\n");
            if (lexer.getType() != LexType.SEMICN) Parser.error();
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
            stmt.flag = 2;
        }
        return stmt;
    }
}
