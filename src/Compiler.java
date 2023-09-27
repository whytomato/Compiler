import Lexer.Lexer;

import java.io.*;

public class Compiler {
    public static void main(String[] args) throws IOException {
        String filePath = "testfile.txt"; // 你的文本文件路径
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Lexer lexer = new Lexer(bufferedReader);
        lexer.createSource();
        String outputFilePath = "output.txt"; // 输出文件的路径
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        writer.write(""); // 写入空字符串，清空文件内容
        while (lexer.next()) {

        }
    }

}
