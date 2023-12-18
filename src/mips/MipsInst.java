package mips;

public class MipsInst {
    private String opcode = "";
    private String operand1 = "";
    private String operand2 = "";
    private String operand3 = "";


    public MipsInst(String opcode, String operand1, String operand2, String operand3) {
        this.opcode = opcode;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operand3 = operand3;
    }

    public String getOpcode() {
        return opcode;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public String getOperand3() {
        return operand3;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("    ").append(opcode);

        if (!operand1.isEmpty()) sb.append(" ").append(operand1);
        if (!operand2.isEmpty()) sb.append(", ").append(operand2);
        if (!operand3.isEmpty()) sb.append(", ").append(operand3);
        sb.append("\n");
        return sb.toString();
    }

}
