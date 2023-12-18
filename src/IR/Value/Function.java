package IR.Value;

import IR.Type.ArrayType;
import IR.Type.PointerType;
import IR.Type.Type;

import java.util.ArrayList;

public class Function extends Constant {
    private ArrayList<BasicBlock> basicBlocks;
    private String memName;
    private ArrayList<Argument> args;

    public Function(String name, Type type) {
        super(name, type);
        this.basicBlocks = new ArrayList<>();
        this.memName = "@" + name;
        this.args = new ArrayList<>();
    }

    public ArrayList<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }

    public void addArgs(Argument arg) {
        this.args.add(arg);
    }

    @Override
    public String getMemName() {
        return memName;
    }

    public ArrayList<Argument> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ");
        sb.append(getType());
        sb.append(" ");
        sb.append(memName);
        sb.append("(");
        Argument argument;
        for (int i = 0; i < args.size(); i++) {
            argument = args.get(i);
            if (argument.getDim() == 0) {
                sb.append(argument.getType()).append(" ").append(argument.getMemName());
            } else if (argument.getDim() == 1) {
                sb.append(argument.getType()).append(" ").append(argument.getMemName());
            } else {

                ArrayType arrayType = (ArrayType) ((PointerType) argument.getType()).getType();
                for (Integer integer : arrayType.getArraySize()) {
                    sb.append("[").append(integer).append(" x ");
                }
                sb.append("i32");
                for (Integer integer : arrayType.getArraySize()) {
                    sb.append("]");
                }
                sb.append(" *").append(argument.getMemName());
            }
//            sb.append(args.get(i).getType()).append(" ").append(args.get(i).getName());
            if (i != args.size() - 1) sb.append(", ");
        }
        sb.append(")");
        sb.append("{\n");
        for (int i = 0; i < basicBlocks.size(); i++) {
            if (i != 0) sb.append(basicBlocks.get(i).getBbName()).append(":\n");
            sb.append(basicBlocks.get(i));
        }
        sb.append("}\n");
        return sb.toString();
    }


}
