import IR.IRModule;
import error.Error;
import lexer.Lexer;
import mips.Mips;
import parser.Parser;

import java.io.*;

public class Compiler {
    public static void main(String[] args) throws IOException {
        String filePath = "testfile.txt"; // 你的文本文件路径
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Lexer.create(bufferedReader);
        Lexer lexer = Lexer.getInstance();
        Error error = Error.getInstance();
//        String outputFilePath = "output.txt"; // 输出文件的路径
        String outputFilePath = "error.txt"; // 输出文件的路径
//        String outputFilePath = "llvm_ir.txt"; // 输出文件的路径
//        String outputFilePath = "mips.txt"; // 输出文件的路径
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
        StringBuilder sb = error.getPrint();
        if (!sb.isEmpty()) {
            printWriter.println(sb);
            printWriter.close();
            return;
        }


        outputFilePath = "llvm_ir.txt";
        fileWriter = new FileWriter(outputFilePath, false);
        printWriter = new PrintWriter(fileWriter);
//        printWriter.print(Parser.stringBuilder);

//        error.errorOutput();


//        TableRoot.getInstance().printf();

        IRModule module = IRModule.getInstance();
//        module.visit();
        printWriter.println(module.visit());
        printWriter.close();


        outputFilePath = "mips.txt";
        fileWriter = new FileWriter(outputFilePath, false);
        printWriter = new PrintWriter(fileWriter);
        Mips mips = Mips.getInstance();
        mips.start();
//        mips.print();

        printWriter.println(mips.print());
        printWriter.close();
    }
}
