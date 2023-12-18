package mips;

import java.util.ArrayList;
import java.util.HashMap;

public class MipsFunction {
    private ArrayList<MipsBB> bbs;
    private String name;
    private int num;
    private HashMap<String, Integer> hashMap;

    public MipsFunction(String name) {
        this.name = name;
        this.bbs = new ArrayList<>();
        this.num = 0;
    }

    public ArrayList<MipsBB> getBbs() {
        return bbs;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n");

        for (int i = 0; i < bbs.size(); i++) {
            if (i != 0) sb.append(bbs.get(i).getName()).append(":\n");
            sb.append(bbs.get(i));
        }
        return sb.toString();
    }
}
