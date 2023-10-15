package symbol;

public class Val implements Symbol {
    private int tableID;
    private String token;
    private int type;
    private int con;

    public Val(int tableID, String token, int con, int type) {
        this.tableID = tableID;
        this.token = token;
        this.con = con;
        this.type = type;
    }

    @Override
    public int getTableID() {
        return tableID;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public int getType() {
        return type;
    }

    public int getCon() {
        return con;
    }

    public void saveType(int type) {
        this.type = type;
    }
}
