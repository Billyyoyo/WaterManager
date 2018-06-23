package cn.e_flo.managewatermeter.dao.callback;

import java.util.List;

import cn.e_flo.managewatermeter.model.AccountBook;
import cn.e_flo.managewatermeter.model.RecordInfo;

public interface DownloadProgressCallback {

    void callback(int bookCount, int infoCount);

}
