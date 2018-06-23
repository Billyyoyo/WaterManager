package cn.eflo.managewatermeter.dao.callback;

import java.util.List;

import cn.eflo.managewatermeter.model.AccountBook;

public interface AccountBooksCallback {
    void callback(List<AccountBook> books);
}
