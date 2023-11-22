package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.Value;

public class IcmpInst extends Instruction {

    private String cond;
    private Value op1;
    private Value op2;
    private String memName;

    public IcmpInst(String name, Type type, Value op1, Value op2) {
        super(name, type);
        this.op1 = op1;
        this.op2 = op2;
    }

    public void setCond(String cond) {
        switch (cond) {
            case "==":
                this.cond = "eq";
                break;
            case "!=":
                this.cond = "ne";
                break;
            case ">":
                this.cond = "sgt";
                break;
            case ">=":
                this.cond = "sge";
                break;
            case "<":
                this.cond = "slt";
                break;
            case "<=":
                this.cond = "sle";
                break;
            default:
                break;
        }

    }

    @Override
    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = "%v" + memName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(memName).append(" = ").append("icmp ");
        sb.append(cond).append(" ").append(getType()).append(" ");
        sb.append(op1.getMemName()).append(", ").append(op2.getMemName());
        return sb.toString();
    }
}
