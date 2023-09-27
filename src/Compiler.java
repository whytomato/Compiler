import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Compiler {
    public static void main(String[] args) {
        String filePath = "testfile.txt"; // 你的文本文件路径

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            Lexer lexer = new Lexer(bufferedReader);
            lexer.createSource();
            while (lexer.next()) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
