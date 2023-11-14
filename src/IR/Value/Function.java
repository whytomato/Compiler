package IR.Value;

import IR.Type.Type;

import java.util.ArrayList;

public class Function extends Constant {
    private ArrayList<BasicBlock> basicBlocks;
    private String memName;

    public Function(String name, Type type) {
        super(name, type);
        this.basicBlocks = new ArrayList<>();
        this.memName = "@" + name;
    }

    public ArrayList<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ");
        sb.append(getType());
        sb.append(" ");
        sb.append(memName);
        sb.append("()");
        sb.append("{\n");
        for (BasicBlock basicBlock : basicBlocks) {
            sb.append(basicBlock);
        }
        sb.append("}\n");
        return sb.toString();
    }
}
