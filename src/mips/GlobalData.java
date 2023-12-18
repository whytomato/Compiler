package mips;

import IR.Value.GlobalValue;
import IR.Value.Value;

import java.util.ArrayList;

public class GlobalData {
    private String name;
    private ArrayList<Integer> numList;
    private static int num = 0;
    private String str;

    public GlobalData() {
        this.name = "";
        this.numList = new ArrayList<>();
    }

    public void analyse(Value value) {
        if (value instanceof GlobalValue globalValue) {
            name = globalValue.getMemName().substring(1);
            if (globalValue.getArrayNum() == null) {
                numList.add(globalValue.getNum());
            } else numList = globalValue.getArrayNum();
        }

    }

    public void stringAnalyse(String string) {
        name = "str" + num;
        num++;
        str = string;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getNumList() {
        return numList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (numList.isEmpty()) {
            sb.append(name).append(": .asciiz");
            sb.append(" \"").append(str).append("\"");
        } else {
            sb.append(name).append(": .word ");
            for (int i = 0; i < numList.size(); i++) {
                sb.append(numList.get(i)).append(",");
            }
        }

        return sb.toString();
    }
}
