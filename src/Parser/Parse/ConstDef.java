package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class ConstDef {
    public String Ident;
    public ArrayList<ConstExp> constExpArrayList;
    public ConstInitVal constInitval;
    static Lexer lexer = Lexer.getInstance();

    public ConstDef() {
        this.Ident = "";
        this.constExpArrayList = new ArrayList<>();
        this.constInitval = null;
    }

    public static ConstDef ConstDefParse() {
        ConstDef constDef = new ConstDef();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        constDef.Ident = lexer.getToken();
        Parser.stringBuilder.append("IDENFR ").append(constDef.Ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            constDef.constExpArrayList.add(ConstExp.ConstExpParse());
            Parser.stringBuilder.append("<ConstExp>\n");
            if (lexer.getType() != LexType.RBRACK) Parser.error();
            Parser.stringBuilder.append("RBRACK ]\n");
            lexer.next();
        }
        if (lexer.getType() != LexType.ASSIGN) Parser.error();
        Parser.stringBuilder.append("ASSIGN =\n");
        lexer.next();
        constDef.constInitval = ConstInitVal.ConstInitValParse();
        Parser.stringBuilder.append("<ConstInitVal>\n");
        return constDef;
    }//ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
}
