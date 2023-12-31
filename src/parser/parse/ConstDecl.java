package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

import java.util.ArrayList;

public class ConstDecl {
    public ArrayList<ConstDef> constDefArrayList;

    public ConstDecl() {
        this.constDefArrayList = new ArrayList<>();
    }

    static Lexer lexer = Lexer.getInstance();
    static Error error = Error.getInstance();

    public static ConstDecl ConstDeclParse() {
        ConstDecl constDecl = new ConstDecl();
        if (lexer.getType() != LexType.CONSTTK) Parser.error();
        Parser.stringBuilder.append("CONSTTK const\n");
        lexer.next();
        if (lexer.getType() != LexType.INTTK) Parser.error();
        Parser.stringBuilder.append("INTTK int\n");
        lexer.next();
        constDecl.constDefArrayList.add(ConstDef.ConstDefParse());
        Parser.stringBuilder.append("<ConstDef>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            Parser.stringBuilder.append("COMMA ,\n");
            lexer.next();
            constDecl.constDefArrayList.add(ConstDef.ConstDefParse());
            Parser.stringBuilder.append("<ConstDef>\n");
        }
        if (lexer.getType() != LexType.SEMICN) error.getError('i', lexer.getLastLine());
        else {
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
        }
        return constDecl;
    }//ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
}
