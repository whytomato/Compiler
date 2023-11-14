package IR;

import IR.Value.User;
import IR.Value.Value;

public class Use {
    private Value value;
    private User user;
    private int pos;

    public Use(Value value, User user) {
        this.user = user;
        this.value = value;
        this.pos = user.getOperands().size();
        user.getOperands().add(value);
        value.getUserList().add(user);
    }
}
