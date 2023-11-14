package IR.Value.Instructions;

import IR.Type.Type;
import IR.Value.User;

public class Instruction extends User {
    private String memName;

    public Instruction(String name, Type type) {
        super(name, type);
    }

    
}
