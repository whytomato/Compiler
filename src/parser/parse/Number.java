package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

public class Number {
    public int intConst;
    static Lexer lexer = Lexer.getInstance();

    public Number() {
        this.intConst = 0;
    }

    public static Number NumberParse() {
        Number number = new Number();
        if (lexer.getType() != LexType.INTCON) Parser.error();
        number.intConst = Integer.parseInt(lexer.getToken());
        Parser.stringBuilder.append("INTCON ").append(number.intConst).append("\n");
        lexer.next();
        return number;
    }//Number → IntConst
}
