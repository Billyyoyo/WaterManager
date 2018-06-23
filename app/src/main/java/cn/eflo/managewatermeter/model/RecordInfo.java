package cn.e_flo.managewatermeter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "readwaterinfo")
public class RecordInfo extends Model {

    public final static int READFLAG_UNDO = 0;
    public final static int READFLAG_DID = 1;

    public final static String[] READFLAGS = {"未抄表", "已抄表"};

    public final static int STATUS_NORMAL = 0;
    public final static int STATUS_STOP = 1;
    public final static int STATUS_UNUSE = 2;
    public final static int STATUS_DISPOSE = 3;

    public final static String[] STATUSFLAGS = {"正常", "停用", "未用", "销户"};

    @Column(name = "Xh")
    public long index;      //序号

    @Column(name = "Yhbh")
    public String userId;   //用户编号

    @Column(name = "Sbbh")
    public String waterMeterId;    //水表编号

    @Column(name = "Sbbj")
    public int waterMeterFlag;    //水表编号

    @Column(name = "Zbbh")
    public String accountBookId;    //账户编号

    @Column(name = "Zbmc")
    public String accountBookName;  //账户名称

    @Column(name = "Yhhm")
    public String userName;     //用户名称

    @Column(name = "Yhdz")
    public String userAddress;  //用户地址

    @Column(name = "Cbbz")
    public int readFlag;        //抄表标识

    @Column(name = "Cbrq")
    public String readDate;     //抄表日期

    @Column(name = "Yslx")
    public String useType;      //用水类型

    @Column(name = "Sbzt")
    public int status;          //水表状态

    @Column(name = "Sydd")
    public int lastMonthNum;    //上月读数

    @Column(name = "Sysl")
    public int lastMonthUse;    //上月用水量

    @Column(name = "Bydd")
    public int thisMonthNum;    //本月读数

    @Column(name = "Bysl")
    public int thisMonthUse;    //本月用水量

    @Column(name = "Bysf")
    public float waterCost;     //本月水费

    @Column(name = "Bywf")
    public float badWaterCost;  //本月污水费

    @Column(name = "Byzy")
    public float waterResourceCost;     //本月水资源费

    @Column(name = "Byqt")
    public float otherCost;     //其他费用

    @Column(name = "Bylj")
    public float garbageCost;   //垃圾费

    @Column(name = "Zjje")
    public float total;         //合计

    @Column(name = "Cbyid")
    public String operatorId;   //抄表员编号

    @Column(name = "Cbymc")
    public String operatorName;     //抄表员名称

    @Column(name = "Bz")
    public String memo;     //备注


}
