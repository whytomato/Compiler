package IR.Value.Instructions;

import IR.Type.IntegerType;
import IR.Type.Type;
import IR.Value.Value;

import java.util.ArrayList;

public class GetElementPtr extends Instruction {
    private Value ptrval;
    private ArrayList<IntegerType> index;

    public GetElementPtr(String name, Type type) {
        super(name, type);
    }
}
