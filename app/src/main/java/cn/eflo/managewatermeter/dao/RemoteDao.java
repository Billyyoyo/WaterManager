package cn.eflo.managewatermeter.dao;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cn.eflo.managewatermeter.dao.callback.AccountBooksCallback;
import cn.eflo.managewatermeter.dao.callback.DownloadDataCallback;
import cn.eflo.managewatermeter.dao.callback.DownloadProgressCallback;
import cn.eflo.managewatermeter.dao.callback.OperatorCallback;
import cn.eflo.managewatermeter.dao.callback.RecordInfoCallback;
import cn.eflo.managewatermeter.dao.callback.UploadDataCallback;
import cn.eflo.managewatermeter.dao.callback.UploadProgressCallback;
import cn.eflo.managewatermeter.model.AccountBook;
import cn.eflo.managewatermeter.model.Operator;
import cn.eflo.managewatermeter.model.RecordInfo;
import cn.eflo.managewatermeter.model.RemoteDBParams;
import cn.eflo.managewatermeter.util.CodeUtil;
import cn.eflo.managewatermeter.util.DateUtil;
import cn.eflo.managewatermeter.util.SystemUtil;
import cn.eflo.managewatermeter.util.Util;
import cn.eflo.managewatermeter.util.WLog;

public class RemoteDao {

    private final static String TAG = "RemoteDao";

    private Connection connection;
    private Context context;

    public RemoteDao(Context context) {
        this.context = context;
    }

    public boolean connect() {
        RemoteDBParams paras = getDBParams();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + paras.address + ":" + paras.port + "/" + paras.dbName //+ "?useUnicode=true&characterEncoding=utf-8"
                    , paras.root
                    , paras.password);
            WLog.i(TAG, "连接成功");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            connection.close();
            WLog.i(TAG, "连接断开");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void getOperator(String operatorName, OperatorCallback callback) {
        CompletableFuture<Operator> future = CompletableFuture.supplyAsync(() -> {
            if (!connect()) {
                return null;
            }
            Operator result = null;
            try {
                String key = Util.toLatin1(operatorName);
                String sql = "Select V_LoginID, V_UserName, V_Password from operator where V_UserName='" + key + "' or V_LoginID='" + key + "'";
                Statement stmt = connection.createStatement();        //创建Statement
                ResultSet rs = stmt.executeQuery(sql);          //ResultSet类似Cursor

                if (rs.next()) {
                    result = new Operator();
                    String name = rs.getString("V_UserName");
                    name = new String(Util.toGBK(name));
                    result.id = rs.getString("V_LoginID");
                    result.name = name;
                    result.password = rs.getString("V_Password");
                    WLog.i(TAG, result + " : " + rs.getString("V_LoginID") + " - " + rs.getString("V_Password"));
                }

                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            disconnect();
            return result;
        });
        future.thenAccept(operator -> callback.callback(operator));
    }

    public void uploadData(List<RecordInfo> infos, UploadProgressCallback progressCallback, UploadDataCallback completeCallback) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            if (!connect()) {
                return null;
            }
            int successCount = 0;
            for (int i = 0; i < infos.size(); i++) {
                try {
                    RecordInfo info = infos.get(i);
                    String sql = "insert into meterinfo (Cbyid,Cbymc,Yhbh,Zbbh,Zbmc,Srrq,Cbrq,Sydd,Bydd,Sbbh,Yhhm,Cbbz) values (?,?,?,?,?,now(),?,?,?,?,?,?)";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, info.operatorId);
                    pst.setString(2, Util.toLatin1(info.operatorName));
                    pst.setString(3, info.userId);
                    pst.setString(4, info.accountBookId);
                    pst.setString(5, Util.toLatin1(info.accountBookName));
                    pst.setString(6, info.readDate);
                    pst.setInt(7, info.lastMonthNum);
                    pst.setInt(8, info.thisMonthNum);
                    pst.setString(9, info.waterMeterId);
                    pst.setString(10, Util.toLatin1(info.userName));
                    pst.setInt(11, info.readFlag);
                    successCount += pst.executeUpdate();
                    progressCallback.callback(successCount);
                    pst.close();
//                    info.status = RecordInfo.READFLAG_UPLOAD;
//                    info.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            disconnect();
            return true;
        });
        future.thenAccept(ret -> {
            completeCallback.callback(ret);
        });
    }

    public void downloadData(String operatorId, DownloadProgressCallback progressCallback, DownloadDataCallback completeCallback) {
        CompletableFuture<DownloadEntry> future = CompletableFuture.supplyAsync(() -> {
            if (!connect()) {
                return null;
            }
            DownloadEntry entry = new DownloadEntry();
            entry.books = getBooks(operatorId, progressCallback);
            entry.infos = getRecords(operatorId, progressCallback);

            disconnect();
            return entry;
        });
        future.thenAccept(entry -> {
            completeCallback.callback(entry.books, entry.infos);
        });
    }

    public List<AccountBook> getBooks(String operatorId, DownloadProgressCallback progressCallback) {
        List<AccountBook> books = new ArrayList<>();
        try {
            String key = Util.toLatin1(operatorId);
            String sql = "SELECT " +
                    "b.V_BookID as BookID," +
                    "a.V_BookName as BookName," +
                    "count(*) as TotalUsers " +
                    "from " +
                    "userinfo a," +
                    "accoutbook b " +
                    "where " +
                    "a.V_BookID=b.V_BookID " +
                    "and b.V_LoginID='" + key + "' " +
                    "and a.N_UserStatus=0 " +
                    "group by " +
                    "a.V_BookID";
            WLog.i(TAG, sql);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Operator operator = LocalDao.getCurrentOperator();
            while (rs.next()) {
                AccountBook book = new AccountBook();
                book.id = rs.getString("BookID");
                String name = rs.getString("BookName");
                book.name = Util.toGBK(name);
                book.total = rs.getInt("TotalUsers");
                book.operator = operator.id;
                books.add(book);
                WLog.i(TAG, "Book: " + book.id + "  " + book.name + "  " + book.total);
                book.save();
                if (progressCallback != null) {
                    progressCallback.callback(books.size(), 0);
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            books = null;
        }
        return books;
    }

    public List<RecordInfo> getRecords(String operatorId, DownloadProgressCallback progressCallback) {
        List<RecordInfo> records = new ArrayList<>();
        try {
            String key = Util.toLatin1(operatorId);
            String sql = "Select " +
                    "a.V_BookID as BookID, " +
                    "a.V_BookName as BookName, " +
                    "a.V_UserID as UserID, " +
                    "a.V_Postcode as PostCode, " +
                    "a.V_UserName as UserName, " +
                    "a.V_Address as Address, " +
                    "a.N_LastMeterNum as LastMeterNum, " +
                    "a.N_Caliber as Caliber, " +
                    "a.N_UserStatus as UserStatus, " +
                    "a.V_WaterTypeName as WaterTypeName " +
                    "from " +
                    "userinfo a, " +
                    "accoutbook b " +
                    "where " +
                    "a.V_BookID=b.V_BookID " +
                    "and b.V_LoginID='" + key + "' " +
                    "and a.N_UserStatus=0 " +
                    "order by " +
                    "a.V_BooKID, a.V_UserID";
            WLog.i(TAG, sql);
            Operator operator = LocalDao.getCurrentOperator();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int i = 1;
            while (rs.next()) {
                RecordInfo record = new RecordInfo();
                record.index = i++;
                record.accountBookId = rs.getString("BookID");
                record.accountBookName = Util.toGBK(rs.getString("BookName"));
                record.userId = rs.getString("UserID");
                record.waterMeterId = rs.getString("PostCode");
                record.userName = Util.toGBK(rs.getString("UserName"));
                record.userAddress = Util.toGBK(rs.getString("Address"));
                record.lastMonthNum = rs.getInt("LastMeterNum");
                record.lastMonthUse = getLastWaterUsed(record.userId);
                record.waterMeterFlag = rs.getInt("Caliber");
                record.status = rs.getInt("UserStatus");
                record.useType = Util.toGBK(rs.getString("WaterTypeName"));
                record.operatorId = operator.id;
                record.operatorName = operator.name;
                records.add(record);
//                WLog.i(TAG, "Record: " + record.accountBookName + "  " + record.userId + "  " + record.userName);
                record.save();
                if (progressCallback != null) {
                    progressCallback.callback(0, records.size());
                }
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            records = null;
        }
        return records;
    }

    private int getLastWaterUsed(String userId) {
        String sql = "Select N_UseNum from usewaterinfo where V_UserID='" + userId + "' order by D_ReadDate  desc limit 1";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int lastUseNum = 0;
            if (rs.next()) {
                lastUseNum = rs.getInt("N_UseNum");
//                WLog.i("LastUse", lastUseNum + "");
            } else {
//                WLog.i("LastUse", "no");
            }
            stmt.close();
            return lastUseNum;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void getAccountBooks(String operatorId, AccountBooksCallback callback) {
        CompletableFuture<List<AccountBook>> future = CompletableFuture.supplyAsync(() -> {
            if (!connect()) {
                return null;
            }
            List<AccountBook> books = getBooks(operatorId, null);
            disconnect();
            return books;
        });
        future.thenAccept(list -> callback.callback(list));
    }

    public void getRecordInfos(String operatorId, RecordInfoCallback callback) {
        CompletableFuture<List<RecordInfo>> future = CompletableFuture.supplyAsync(() -> {
            if (!connect()) {
                return null;
            }
            List<RecordInfo> records = getRecords(operatorId, null);
            disconnect();
            return records;
        });
        future.thenAccept(list -> callback.callback(list));
    }

    public void saveDBParams(RemoteDBParams params) {
        SharedPreferences settings = context.getSharedPreferences("db_params", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("dbname", params.dbName);
        editor.putString("address", params.address);
        editor.putString("port", params.port);
        editor.putString("root", params.root);
        editor.putString("password", params.password);
        editor.commit();
    }

    public RemoteDBParams getDBParams() {
        SharedPreferences settings = context.getSharedPreferences("db_params", Context.MODE_PRIVATE);
        RemoteDBParams params = new RemoteDBParams();
        if (settings.contains("dbname")) {
            params.dbName = settings.getString("dbname", "");
            params.address = settings.getString("address", "");
            params.port = settings.getString("port", "");
            params.root = settings.getString("root", "");
            params.password = settings.getString("password", "");
        } else {
            params.dbName = SystemUtil.getApplicationMetaString(context, "DBNAME");
            params.address = SystemUtil.getApplicationMetaString(context, "ADDRESS");
            params.port = SystemUtil.getApplicationMetaInt(context, "PORT");
            params.root = SystemUtil.getApplicationMetaString(context, "LOGINNAME");
            params.password = SystemUtil.getApplicationMetaString(context, "PASSWORD");
        }
        return params;
    }

    private class DownloadEntry {
        List<RecordInfo> infos;
        List<AccountBook> books;
    }

}
