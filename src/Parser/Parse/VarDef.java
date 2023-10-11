package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class VarDef {
    public String ident;
    public ArrayList<ConstExp> constExpArrayList;
    public InitVal initVal;
    static Lexer lexer = Lexer.getInstance();

    public VarDef() {
        this.ident = "";
        this.constExpArrayList = new ArrayList<>();
        this.initVal = null;
    }

    public static VarDef VarDefParse() {
        VarDef varDef = new VarDef();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        varDef.ident = lexer.getToken();
        Parser.stringBuilder.append("IDENFR ").append(varDef.ident).append("\n");
        lexer.next();
        while (true) {
            if (lexer.getType() != LexType.LBRACK) break;
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            varDef.constExpArrayList.add(ConstExp.ConstExpParse());
            Parser.stringBuilder.append("<ConstExp>\n");
            if (lexer.getType() != LexType.RBRACK) Parser.error();
            Parser.stringBuilder.append("RBRACK ]\n");
            lexer.next();
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
