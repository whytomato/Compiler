package error;

public class ErrorType {
    char type;
    int lineNum;
    String detail;

    public ErrorType(char type, int lineNum) {
        this.type = type;
        this.lineNum = lineNum;
        switch (type) {
            case 'a':
                this.detail = "非法符号";
                break;
            case 'b':
                this.detail = "名字重定义";
                break;
            case 'c':
                this.detail = "未定义的名字";
                break;
            case 'd':
                this.detail = "函数参数不匹配";
                break;
            case 'e':
                this.detail = "函数参数类型不匹配";
                break;
            case 'f':
                this.detail = "无返回值出现return";
                break;
            case 'g':
                this.detail = "有返回值缺少return";
                break;
            case 'h':
                this.detail = "不能改变常量的值";
                break;
            case 'i':
                this.detail = "缺少分号";
                break;
            case 'j':
                this.detail = "缺少右小括号";
                break;
            case 'k':
                this.detail = "缺少右中括号";
                break;
            case 'l':
                this.detail = "printf数量不匹配";
                break;
            case 'm':
                this.detail = "非循环中出现break和continue";
                break;
            default:
                break;
        }
    }
}
