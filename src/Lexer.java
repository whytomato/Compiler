import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Lexer {
    public Lexer(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
    }

    private String source;
    private int p;
    private String token;
    private LexType type;
    private int line;
    private int num;
    private final String[] key = {"main", "const", "int", "break", "continue", "if",
            "else", "for", "getint", "printf", "return", "void"};
    private final LexType[] lexTypes = LexType.values();

    private final BufferedReader bufferedReader;

    public void createSource() throws IOException {
        int currentChar;
        StringBuilder stringBuilder = new StringBuilder();
        while ((currentChar = bufferedReader.read()) != -1) {
            char c = (char) currentChar;
            stringBuilder.append(c);
        }
        bufferedReader.close(); // 关闭文件
        source = stringBuilder.toString();
        line = 1;
//        p = source.charAt(0);
        p = 0;
        token = "";
        type = null;
        num = 0;
    }

    public boolean next() {
        String outputFilePath = "output.txt"; // 输出文件的路径
        getToken();
        if (type != null) {
            if (type == LexType.INTCON) {

                System.out.println(type + " " + num);
            } else {
                System.out.println(type + " " + token);
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(outputFilePath, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            if (type != null) {
                if (type == LexType.INTCON) {
                    printWriter.println(type + " " + num);
                } else {
                    printWriter.println(type + " " + token);
                }
            }

            // 关闭文件输出流
            printWriter.close();
//            System.out.println("文本已成功写入到 " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        type = null;
        token = "";
        return p < source.length();
    }

    public void getToken() {
//        StringBuilder tokenBuilder = new StringBuilder();
        char c = source.charAt(p);
        if (c == '_' || Character.isLetter(c)) {
            token += c;
            p++;
            while (p < source.length() && (source.charAt(p) == '_' || Character.isLetter(source.charAt(p)) ||
                    Character.isDigit(source.charAt(p)))) {
                c = source.charAt(p++);
                token += c;
            }
            reserve();
        } else if (Character.isDigit(c)) {
            token += c;
            p++;
            while (p < source.length() && (Character.isDigit(source.charAt(p)))) {
                c = source.charAt(p++);
                token += c;
            }
            type = LexType.INTCON;
            num = Integer.parseInt(token);

        } else if (c == '"') {
            token += c;
            p++;
            while (p < source.length() && source.charAt(p) != '"') {
                c = source.charAt(p++);
                token += c;
            }
            token += '"';
            type = LexType.STRCON;
            p++;
        } else if (c == '!' || c == '<' || c == '>' || c == '=') {
            token += c;
            p++;
            if (p < source.length()) {
                c = source.charAt(p);
                if (c == '=') {
                    token += c;
                    p++;
                }
            }
            switch (token) {
                case "!":
                    type = LexType.NOT;
                    break;
                case "!=":
                    type = LexType.NEQ;
                    break;
                case "<":
                    type = LexType.LSS;
                    break;
                case "<=":
                    type = LexType.LEQ;
                    break;
                case ">":
                    type = LexType.GRE;
                    break;
                case ">=":
                    type = LexType.GEQ;
                    break;
                case "=":
                    type = LexType.ASSIGN;
                    break;
                case "==":
                    type = LexType.EQL;
                    break;
            }
        } else if (c == '&') {
            p++;
            if (p < source.length()) {
                c = source.charAt(p);
                if (c == '&') {
                    token = "&&";
                    type = LexType.AND;
                }
            }
        } else if (c == '|') {
            p++;
            if (p < source.length()) {
                c = source.charAt(p);
                if (c == '|') {
                    token = "||";
                    type = LexType.OR;
                }
            }
        } else if (c == '/') {
            p++;
            if (p < source.length() && source.charAt(p) == '/') {
                p++;
                while (p < source.length() && source.charAt(p) != '\n') {
                    p++;
                }
                if (p < source.length() && source.charAt(p) == '\n') {
                    line++;
                }
                p++;
            } else if (p < source.length() && source.charAt(p) == '*') {
                int p1 = p, p2 = p + 1;
                while (p2 < source.length() && (!(source.charAt(p1) == '*' && source.charAt(p2) == '/'))) {
                    p++;
                    p1++;
                    p2++;
                }
                p = p2 + 1;
            } else {
                token = "/";
                type = LexType.DIV;
            }

        } else if (c == '+') {
            p++;
            token += c;
            type = LexType.PLUS;
        } else if (c == '-') {
            p++;
            token += c;
            type = LexType.MINU;
        } else if (c == '*') {
            p++;
            token += c;
            type = LexType.MULT;
        } else if (c == '%') {
            p++;
            token += c;
            type = LexType.MOD;
        } else if (c == ';') {
            p++;
            token += c;
            type = LexType.SEMICN;
        } else if (c == ',') {
            p++;
            token += c;
            type = LexType.COMMA;
        } else if (c == '(') {
            p++;
            token += c;
            type = LexType.LPARENT;
        } else if (c == ')') {
            p++;
            token += c;
            type = LexType.RPARENT;
        } else if (c == '[') {
            p++;
            token += c;
            type = LexType.LBRACK;
        } else if (c == ']') {
            p++;
            token += c;
            type = LexType.RBRACK;
        } else if (c == '{') {
            p++;
            token += c;
            type = LexType.LBRACE;
        } else if (c == '}') {
            p++;
            token += c;
            type = LexType.RBRACE;
        } else {
            p++;
        }
    }

    public void reserve() {
        int index = -1;
        for (String string : key) {
            index++;
            if (string.equals(token)) {
                type = lexTypes[index];
                return;
            }
        }
        type = LexType.IDENFR;
    }
}
