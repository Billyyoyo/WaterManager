package cn.e_flo.managewatermeter.dao.callback;

import java.util.List;

import cn.e_flo.managewatermeter.model.AccountBook;

public interface AccountBooksCallback {
    void callback(List<AccountBook> books);
}
