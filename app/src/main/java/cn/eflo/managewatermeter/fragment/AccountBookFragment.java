package cn.e_flo.managewatermeter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import cn.e_flo.managewatermeter.R;
import cn.e_flo.managewatermeter.dao.LocalDao;
import cn.e_flo.managewatermeter.dao.RemoteDao;
import cn.e_flo.managewatermeter.event.DownloadCompleteEvent;
import cn.e_flo.managewatermeter.event.EventBus;
import cn.e_flo.managewatermeter.model.Operator;
import cn.e_flo.managewatermeter.util.WLog;

public class AccountBookFragment extends DefaultFragment {

    private int mColumnCount = 1;

    AccountBookAdapter adapter = new AccountBookAdapter();

    public AccountBookFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accountbooks, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter.setData(LocalDao.getAccountBooks());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
//        RemoteDao dao = new RemoteDao(getContext());
//        Operator operator = LocalDao.getCurrentOperator();
//        dao.getAccountBooks(operator.id, list -> {
//            adapter.setData(list);
//        });
//        dao.getRecords(operator.id, list -> {
//            WLog.i("Test", list.size() + "");
//        });
    }

    @Subscribe
    public void whenDownloadComplete(DownloadCompleteEvent ev) {

    }

    @Override
    public void onDestroy() {
        EventBus.unregister(this);
        super.onDestroy();
    }

}
