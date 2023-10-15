package error;

import java.util.ArrayList;

public class Error {
    public ArrayList<ErrorType> errorList;
    public ArrayList<ErrorType> newList;
    private static Error Error = new Error();
    private StringBuilder stringBuilder;

    public Error() {
        this.errorList = new ArrayList<>();
        this.newList = new ArrayList<>();
        this.stringBuilder = new StringBuilder();
    }

    public static Error getInstance() {
        return Error;
    }

    public void getError(char c, int num) {
        errorList.add(new ErrorType(c, num));
    }

    public void errorOutput() {
        makeNew();
        for (ErrorType errorType : newList) {
            stringBuilder.append(errorType.lineNum).append(" ").append(errorType.type).append('\n');
            System.out.println(errorType.lineNum + " " + errorType.detail);
        }
    }

    public void makeNew() {
        int j = 0;
        for (int i = 0; i < errorList.size(); i++) {
            if (j == 0) {
                newList.add(errorList.get(i));
                j++;
            } else {
                if (errorList.get(i).lineNum != newList.get(j - 1).lineNum) {
                    newList.add(errorList.get(i));
                    j++;
                }
            }
        }
    }

    public StringBuilder getPrint() {
        errorOutput();
        return stringBuilder;
    }
}
