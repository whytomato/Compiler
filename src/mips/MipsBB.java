package mips;

import java.util.ArrayList;

public class MipsBB {
    private ArrayList<MipsInst> mipsInsts;
    private String name;

    public MipsBB(String name) {
        this.name = name;
        this.mipsInsts = new ArrayList<>();
    }

    public ArrayList<MipsInst> getMipsInsts() {
        return mipsInsts;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MipsInst mipsInst : mipsInsts) {
            sb.append(mipsInst);
        }
        return sb.toString();
    }
}
