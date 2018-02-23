package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Regions")
public class Region {
    @Key(columnName = "REG_NUM")
    private int reg_num;
    private String reg_name;

    @DaoConstructor
    public Region(int reg_num, String reg_name) {
        this.reg_num = reg_num;
        this.reg_name = reg_name;
    }
}
