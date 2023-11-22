package IR.Value;

import IR.Value.Instructions.Instruction;

import java.util.ArrayList;

public class BasicBlock {
    private ArrayList<Instruction> instructions;
    private static int bbNum = -1;
    private String bbName;

    public BasicBlock() {
        this.instructions = new ArrayList<>();
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Instruction instruction : instructions) {
            sb.append(instruction).append("\n");
        }
        return sb.toString();
    }

    public void setBbName() {
        bbNum += 1;
        bbName = "B" + bbNum;
    }

    public String getBbName() {
        return bbName;
    }

    public BasicBlock clone() {
        BasicBlock basicBlock = new BasicBlock();
        basicBlock.bbName = this.bbName;
        basicBlock.instructions = new ArrayList<>(this.instructions);
        return basicBlock;
    }
}
