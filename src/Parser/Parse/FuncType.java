package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class FuncType {
    public String funcType;
    static Lexer lexer = Lexer.getInstance();

    public FuncType() {
        this.funcType = "";
    }

    public static FuncType FuncTypeParse() {
        FuncType funcType = new FuncType();
        if (lexer.getType() == LexType.VOIDTK) {
            funcType.funcType = lexer.getToken();
            Parser.stringBuilder.append("VOIDTK ").append(funcType.funcType).append("\n");
        } else if (lexer.getType() == LexType.INTTK) {
            funcType.funcType = lexer.getToken();
            Parser.stringBuilder.append("INTTK ").append(funcType.funcType).append("\n");
        } else Parser.error();
        lexer.next();
        return funcType;
    }//FuncType → 'void' | 'int'
}
