package Parser;

import Lexer.Lexer;
import Parser.Parse.CompUnit;

import java.io.IOException;

public class Parser {
    static Lexer lexer = Lexer.getInstance();
    public static CompUnit compUnit = new CompUnit();
    public static StringBuilder stringBuilder = new StringBuilder();

    public void parse() throws IOException {
        compUnit.CompUnitParse();
        Parser.stringBuilder.append("<CompUnit>\n");
        printf();
    }

    public static void error() {
        System.out.println(lexer.getToken());
        System.out.println(lexer.getLine());
        System.out.println("Parser.error");
    }


    public void printf() {
//        System.out.println(stringBuilder);
    }
}
