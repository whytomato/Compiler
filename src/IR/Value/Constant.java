package IR.Value;

import IR.Type.Type;

public class Constant extends User {
    private int num;
    private String memName;

    public Constant(String name, Type type) {
        super(name, type);
        this.memName = name;
    }


    @Override
    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

}
