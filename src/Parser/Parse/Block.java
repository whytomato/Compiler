package Parser.Parse;

import Lexer.LexType;
import Lexer.Lexer;
import Parser.Parser;

import java.util.ArrayList;

public class Block {
    public ArrayList<BlockItem> blockItemArrayList;
    static Lexer lexer = Lexer.getInstance();

    public Block() {
        this.blockItemArrayList = new ArrayList<>();
    }

    public static Block BlockParse() {
        Block block = new Block();
        if (lexer.getType() != LexType.LBRACE) Parser.error();
        Parser.stringBuilder.append("LBRACE {\n");
        lexer.next();
        while (true) {
            if (lexer.getType() == LexType.RBRACE) break;
            block.blockItemArrayList.add(BlockItem.BlockItemParse());
        }
        Parser.stringBuilder.append("RBRACE }\n");
        lexer.next();
        return block;
    }
}
