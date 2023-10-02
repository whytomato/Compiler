package Lexer;

import java.io.BufferedReader;
import java.io.IOException;

public class Lexer {

    private static Lexer lexer = null;

    private Lexer(BufferedReader bufferedReader) throws IOException {
        int currentChar;
        StringBuilder stringBuilder = new StringBuilder();
        while ((currentChar = bufferedReader.read()) != -1) {
            char c = (char) currentChar;
            stringBuilder.append(c);
        }
        bufferedReader.close(); // 关闭文件
        source = stringBuilder.toString();
        line = 1;
        p = 0;
        token = "";
        type = null;
        num = 0;
    }

    private String source;
    private int p;
    private int lastP;
    private String preRead;
    private LexType preType;
    private String prePreRead;
    private LexType prePreType;
    private String token;
    private String lastToken;
    private LexType type;
    private LexType lastType;
    private int line;
    private int num;
    private final String[] key = {"main", "const", "int", "break", "continue", "if",
            "else", "for", "getint", "printf", "return", "void"};
    private final LexType[] lexTypes = LexType.values();


    public static void create(BufferedReader bufferedReader) throws IOException {
        if (lexer == null) {
            lexer = new Lexer(bufferedReader);
        }
    }

    public static Lexer getInstance() {
        return lexer;
    }

    public boolean next() {
        boolean flag = p < source.length();
        if (!flag) {
            return false;
        }
        type = null;
        token = "";
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
                    p++;
                }
            }
        } else if (c == '|') {
            p++;
            if (p < source.length()) {
                c = source.charAt(p);
                if (c == '|') {
                    token = "||";
                    type = LexType.OR;
                    p++;
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
                return next();
            } else if (p < source.length() && source.charAt(p) == '*') {
                int p1 = p, p2 = p + 1;
                while (p2 < source.length() && (!(source.charAt(p1) == '*' && source.charAt(p2) == '/'))) {
                    p++;
                    p1++;
                    p2++;
                }
                p = p2 + 1;
                return next();
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
            return next();
        }
        return flag;
    }

    public String getToken() {
        return token;
    }

    public LexType getType() {
        return type;
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

    public void back() {
        p = lastP;
        token = lastToken;
        type = lastType;
    }

    public void pre(int n) {
        lastP = p;
        lastToken = token;
        lastType = type;
        for (int i = 0; i < n; i++) {
            next();
            if (i == 0) {
                preRead = getToken();
                preType = getType();
            } else {
                if (p <= source.length()) {
                    prePreRead = getToken();
                    prePreType = getType();
                }
            }
        }
    }

    public String getPreRead() {
        lastP = p;
        lastToken = token;
        lastType = type;
        if (next()) {
            preRead = getToken();
            back();
        }
        return preRead;
    }

    public String getPrePreRead() {
        lastP = p;
        lastToken = token;
        lastType = type;
        if (next()) {
            if (next()) {
                prePreRead = getToken();
                back();
            }
        }
        return prePreRead;
    }

    public LexType getPreType() {
        lastP = p;
        lastToken = token;
        lastType = type;
        if (next()) {
            preType = getType();
            back();
        }
        return preType;
    }

    public LexType getPrePreType() {
        lastP = p;
        lastToken = token;
        lastType = type;
        if (next()) {
            if (next()) {
                prePreType = getType();
                back();
            }
        }
        return prePreType;
    }

    public boolean judge() {
        int a = p;
        while (a < source.length()) {
            if (source.charAt(a) == ';') {
                break;
            } else {
                if (source.charAt(a) == '=') return true;
            }
            a++;
        }
        return false;
    }
}
