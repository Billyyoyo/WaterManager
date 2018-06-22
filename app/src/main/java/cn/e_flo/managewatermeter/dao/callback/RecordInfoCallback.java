package cn.e_flo.managewatermeter.dao.callback;

import java.util.List;

import cn.e_flo.managewatermeter.model.RecordInfo;

public interface RecordInfoCallback {

    void callback(List<RecordInfo> infos);

}
