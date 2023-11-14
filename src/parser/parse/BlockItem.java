package parser.parse;

import lexer.LexType;
import lexer.Lexer;
import parser.Parser;

public class BlockItem {
    public Decl decl;
    public Stmt stmt;
    static Lexer lexer = Lexer.getInstance();

    public BlockItem() {
        this.decl = null;
        this.stmt = null;
    }

    public static BlockItem BlockItemParse() {
        BlockItem blockItem = new BlockItem();
        if (lexer.getType() == LexType.CONSTTK || lexer.getType() == LexType.INTTK) {
            blockItem.decl = Decl.DeclParse();
        } else {
            blockItem.stmt = Stmt.StmtParse();
            Parser.stringBuilder.append("<Stmt>\n");
        }
        return blockItem;
    }//BlockItem → Decl | Stmt
}
