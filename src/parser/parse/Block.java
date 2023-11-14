package parser.parse;

import error.Error;
import lexer.LexType;
import lexer.Lexer;
import parser.Parser;
import symbol.Func;
import symbol.TableRoot;
import symbol.Val;

import java.util.ArrayList;

public class Block {
    public ArrayList<BlockItem> blockItemArrayList;
    static Lexer lexer = Lexer.getInstance();
    static TableRoot tableRoot = TableRoot.getInstance();
    static Error error = Error.getInstance();


    public Block() {
        this.blockItemArrayList = new ArrayList<>();
    }

//    public static Block BlockParse() {
//        Block block = new Block();
//        if (lexer.getType() != LexType.LBRACE) Parser.error();
//        tableRoot.save();
//        Parser.stringBuilder.append("LBRACE {\n");
//        lexer.next();
//        BlockItem blockItem = new BlockItem();
//        while (true) {
//            if (lexer.getType() == LexType.RBRACE) break;
//            blockItem = BlockItem.BlockItemParse();
//            block.blockItemArrayList.add(blockItem);
//        }
//        if (!blockItem.stmt.hasReturn) error.getError('g', lexer.getLine());
//        Parser.stringBuilder.append("RBRACE }\n");
////        tableRoot.printf();
////        System.out.println();
//        tableRoot.delete();
//        lexer.next();
//        return block;
//    }

    public static Block BlockParse(boolean flag) {
        Block block = new Block();
        if (lexer.getType() != LexType.LBRACE) Parser.error();
        if (!flag) {
            tableRoot.createTable();
        }
        Parser.stringBuilder.append("LBRACE {\n");
        lexer.next();
        BlockItem blockItem = new BlockItem();
        while (true) {
            if (lexer.getType() == LexType.RBRACE) break;
            blockItem = BlockItem.BlockItemParse();
            block.blockItemArrayList.add(blockItem);
        }

        if (!(tableRoot.getSymbol(tableRoot.getSymbolTable().getFatherName()) instanceof Val)) {
            Func func = (Func) tableRoot.getSymbol(tableRoot.getSymbolTable().getFatherName());
            if (flag && (blockItem.stmt == null || !blockItem.stmt.hasReturn)
                    && func.getType() == -1 && func.getRetype() == 0)
                error.getError('g', lexer.getLine());
        }
//        tableRoot.printf();
//        System.out.println();
        tableRoot.delete();

        Parser.stringBuilder.append("RBRACE }\n");

        lexer.next();
        return block;
    }
}
