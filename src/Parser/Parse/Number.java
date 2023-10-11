package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

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
