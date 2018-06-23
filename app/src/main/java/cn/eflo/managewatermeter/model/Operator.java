package cn.eflo.managewatermeter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "operator")
public class Operator extends Model {

    public final static int FLAG_UNLOG = 0;
    public final static int FLAG_LOGGED = 1;

    @Column(name = "oid" )
    public String id;

    @Column(name = "name")
    public String name;

    public String password;

    @Column(name = "flag")
    public int flag;


}
