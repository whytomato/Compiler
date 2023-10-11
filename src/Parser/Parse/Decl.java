package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

public class Decl {
    public ConstDecl constDecl;
    public VarDecl varDecl;
    static Lexer lexer = Lexer.getInstance();

    public Decl() {
        this.constDecl = null;
        this.varDecl = null;
    }

    public static Decl DeclParse() {
        Decl decl = new Decl();
        if (lexer.getType() == LexType.CONSTTK) {
            decl.constDecl = ConstDecl.ConstDeclParse();
            Parser.stringBuilder.append("<ConstDecl>\n");
        } else {
            decl.varDecl = VarDecl.VarDeclParse();
            Parser.stringBuilder.append("<VarDecl>\n");
        }
        return decl;
    }//Decl → ConstDecl | VarDecl
}
