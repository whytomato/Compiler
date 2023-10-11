package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class UnaryOp {
    public String unaryOp;
    static Lexer lexer = Lexer.getInstance();

    public UnaryOp() {
        this.unaryOp = "";
    }

    public static UnaryOp UnaryOpParse() {
        UnaryOp unaryOp = new UnaryOp();
        if (lexer.getType() != LexType.PLUS && lexer.getType() != LexType.MINU && lexer.getType() != LexType.NOT)
            Parser.error();
        unaryOp.unaryOp = lexer.getToken();
        if (lexer.getType() == LexType.PLUS) {
            Parser.stringBuilder.append("PLUS ").append(unaryOp.unaryOp).append("\n");
        } else if (lexer.getType() == LexType.MINU) {
            Parser.stringBuilder.append("MINU ").append(unaryOp.unaryOp).append("\n");
        } else if (lexer.getType() == LexType.NOT) {
            Parser.stringBuilder.append("NOT ").append(unaryOp.unaryOp).append("\n");
        }
        lexer.next();
        return unaryOp;
    }//UnaryOp → '+' | '−' | '!'
}
