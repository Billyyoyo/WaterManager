package cn.eflo.managewatermeter.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "accountbook")
public class AccountBook extends Model {

    @Column(name = "bid")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "total")
    public int total;

    @Column(name = "complete")
    public int complete;

    @Column(name = "operator")
    public String operator;

}
