package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.Value;

import java.util.ArrayList;

public class CallInst extends Instruction {
    private ArrayList<Value> args;
    private String memName;

    public CallInst(String name, Type type) {
        super(name, type);
        this.args = new ArrayList<>();
    }

    public void setArgs(ArrayList<Value> args) {
        this.args = args;
    }

    @Override
    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (memName != null) {
            sb.append("    ").append(getMemName()).append(" = call ");
        } else sb.append("    ").append("call ");
        sb.append(getType()).append(" ").append(getName());
        sb.append("(");
        if (!args.isEmpty()) {
            for (int i = 0; i < args.size(); i++) {
                sb.append(args.get(i).getType()).append(" ").append(args.get(i).getMemName());
                if (i != args.size() - 1) sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public ArrayList<Value> getArgs() {
        return args;
    }
}
