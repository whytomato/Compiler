package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.Value;

public class BrInst extends Instruction {
    private Value cond;
    private String ifTrue, ifFalse;
    private String dest;

    public BrInst(String name, Type type, Value cond) {
        super(name, type);
        this.cond = cond;
    }

    public void setDest(String dest) {
        this.dest = "%" + dest;
    }

    public void setLabels(String ifTrue, String ifFalse) {
        this.ifTrue = "%" + ifTrue;
        this.ifFalse = "%" + ifFalse;
    }

    public Value getCond() {
        return cond;
    }

    public String getIfTrue() {
        return ifTrue;
    }

    public String getIfFalse() {
        return ifFalse;
    }

    public String getDest() {
        return dest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    br ");
        if (cond == null) {
            sb.append("label ").append(dest);
        } else {
            sb.append("i1 ").append(cond.getMemName());
            sb.append(", ").append("label ").append(ifTrue);
            sb.append(", ").append("label ").append(ifFalse);
        }

        return sb.toString();
    }
}
