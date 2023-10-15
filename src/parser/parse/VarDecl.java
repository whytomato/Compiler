package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

import java.util.ArrayList;

public class VarDecl {
    public ArrayList<VarDef> varDefArrayList;
    static Lexer lexer = Lexer.getInstance();
    static Error error = Error.getInstance();

    public VarDecl() {
        this.varDefArrayList = new ArrayList<>();
    }

    public static VarDecl VarDeclParse() {
        VarDecl varDecl = new VarDecl();
        if (lexer.getType() != LexType.INTTK) Parser.error();
        Parser.stringBuilder.append("INTTK int\n");
        lexer.next();
        varDecl.varDefArrayList.add(VarDef.VarDefParse());
        Parser.stringBuilder.append("<VarDef>\n");
        while (true) {
            if (lexer.getType() != LexType.COMMA) break;
            Parser.stringBuilder.append("COMMA ,\n");
            lexer.next();
            varDecl.varDefArrayList.add(VarDef.VarDefParse());
            Parser.stringBuilder.append("<VarDef>\n");
        }
        if (lexer.getType() != LexType.SEMICN) error.getError('i', lexer.getLastLine());
        else {
            Parser.stringBuilder.append("SEMICN ;\n");
            lexer.next();
        }
        return varDecl;
    }//VarDecl → BType VarDef { ',' VarDef } ';'
}
