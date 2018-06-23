package cn.eflo.managewatermeter.dao.callback;

import java.util.List;

import cn.eflo.managewatermeter.model.AccountBook;
import cn.eflo.managewatermeter.model.RecordInfo;

public interface DownloadProgressCallback {

    void callback(int bookCount, int infoCount);

}
