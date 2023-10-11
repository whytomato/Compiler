package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class FuncFParam {
    public String ident;
    public ArrayList<ConstExp> constExpArrayList;
    static Lexer lexer = Lexer.getInstance();

    public FuncFParam() {
        this.ident = "";
        this.constExpArrayList = new ArrayList<>();
    }

    public static FuncFParam FuncFParamParse() {
        FuncFParam funcFParam = new FuncFParam();
        if (lexer.getType() != LexType.INTTK) Parser.error();
        Parser.stringBuilder.append("INTTK int\n");
        lexer.next();
        if (lexer.getType() != LexType.IDENFR) Parser.error();
        funcFParam.ident = lexer.getToken();
        Parser.stringBuilder.append("IDENFR ").append(funcFParam.ident).append("\n");
        lexer.next();
        if (lexer.getType() == LexType.LBRACK) {
            Parser.stringBuilder.append("LBRACK [\n");
            lexer.next();
            if (lexer.getType() != LexType.RBRACK) Parser.error();
            Parser.stringBuilder.append("RBRACK ]\n");
            lexer.next();
            while (true) {
                if (lexer.getType() != LexType.LBRACK) break;
                Parser.stringBuilder.append("LBRACK [\n");
                lexer.next();
                funcFParam.constExpArrayList.add(ConstExp.ConstExpParse());
                Parser.stringBuilder.append("<ConstExp>\n");
                if (lexer.getType() != LexType.RBRACK) Parser.error();
                Parser.stringBuilder.append("RBRACK ]\n");
                lexer.next();
            }
        }
        return funcFParam;
    }//FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
}
