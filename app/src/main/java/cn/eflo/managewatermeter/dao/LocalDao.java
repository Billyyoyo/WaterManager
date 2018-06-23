package cn.eflo.managewatermeter.dao;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

import cn.eflo.managewatermeter.model.AccountBook;
import cn.eflo.managewatermeter.model.Operator;
import cn.eflo.managewatermeter.model.RecordInfo;

public class LocalDao {

    public static Operator getCurrentOperator() {
        Operator operator = new Select().from(Operator.class).where("flag=?", Operator.FLAG_LOGGED).executeSingle();
        return operator;
    }

    public static void resetOperatorsFlag() {
        new Update(Operator.class).set("flag=?", Operator.FLAG_UNLOG).execute();
    }

    public static void clearHistoryData() {
        new Delete().from(AccountBook.class).execute();
        new Delete().from(RecordInfo.class).execute();
    }

    public static List<AccountBook> getAccountBooks() {
        return new Select().from(AccountBook.class).where("operator=?", getCurrentOperator().id).execute();
    }

    public static List<RecordInfo> getRecordInfos(String bookId){
        return new Select().from(RecordInfo.class).where("Zbbh=?", bookId).execute();
    }

    public static List<RecordInfo> getDidInfos(){
        return new Select().from(RecordInfo.class).where("Cbbz=?", RecordInfo.READFLAG_DID).execute();
    }


}
