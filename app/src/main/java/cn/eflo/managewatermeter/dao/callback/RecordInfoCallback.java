package cn.eflo.managewatermeter.dao.callback;

import java.util.List;

import cn.eflo.managewatermeter.model.RecordInfo;

public interface RecordInfoCallback {

    void callback(List<RecordInfo> infos);

}
