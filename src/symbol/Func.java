package symbol;

import java.util.ArrayList;

public class Func implements Symbol {
    private int tableID;
    private String token;
    private int type;
    private int retype;
    //    private int paramNum;
    private ArrayList<Integer> paramTypeList;

    public Func(int tableID, String token, int type, int retype,
                ArrayList<Integer> paramTypeList) {
        this.tableID = tableID;
        this.token = token;
        this.type = type;
        this.retype = retype;
        this.paramTypeList = paramTypeList;
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

    public int getRetype() {
        return retype;
    }

    public ArrayList<Integer> getParamTypeList() {
        return paramTypeList;
    }

}
