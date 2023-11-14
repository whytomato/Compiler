package IR.Value;

import IR.Value.Instructions.Instruction;

import java.util.ArrayList;

public class BasicBlock {
    ArrayList<Instruction> instructions;

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
}
