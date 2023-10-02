import Lexer.Lexer;
import Parser.Parser;

import java.io.*;

public class Compiler {
    public static void main(String[] args) throws IOException {
        String filePath = "testfile.txt"; // 你的文本文件路径
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Lexer.create(bufferedReader);
        Lexer lexer = Lexer.getInstance();
        String outputFilePath = "output.txt"; // 输出文件的路径
        FileWriter fileWriter = new FileWriter(outputFilePath, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        Parser parser = new Parser();
        while (lexer.next()) {
            parser.parse();
//            if (lexer.getType() != null) {
//                if (lexer.getType() == LexType.INTCON) {
//                    printWriter.println(lexer.getType() + " " + lexer.getToken());
//                } else {
//                    printWriter.println(lexer.getType() + " " + lexer.getToken());
//                }
//            }
        }
        printWriter.print(parser.stringBuilder);
        printWriter.close();
    }
}
