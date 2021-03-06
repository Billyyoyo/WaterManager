package cn.eflo.managewatermeter.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.eflo.managewatermeter.R;
import cn.eflo.managewatermeter.dao.LocalDao;
import cn.eflo.managewatermeter.dao.RemoteDao;
import cn.eflo.managewatermeter.event.RemoteCompleteEvent;
import cn.eflo.managewatermeter.event.EventBus;

public class DownloadDataFragment extends DefaultFragment {

    @BindView(R.id.progress_view)
    ProgressBar progressView;
    @BindView(R.id.message_view)
    TextView messageView;
    @BindView(R.id.book_count_view)
    TextView bookCountView;
    @BindView(R.id.user_count_view)
    TextView userCountView;
    @BindView(R.id.download_btn)
    TextView doView;

    public DownloadDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_data, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        progressView.setVisibility(View.INVISIBLE);
        messageView.setVisibility(View.INVISIBLE);
        bookCountView.setVisibility(View.INVISIBLE);
        userCountView.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.download_btn)
    public void toDownload() {
        if(doView.isSelected()){
            EventBus.post(new RemoteCompleteEvent(RemoteCompleteEvent.DOWNLOAD));
            return;
        }
        new AlertDialog.Builder(getContext())
                .setTitle("下载数据")
                .setMessage("警告：下载数据会清空已经抄表的数据和历史数据，请确认目前抄表的数据已经上传完毕或需要开始新一轮抄表工作才需要下载！ \n\n您确定要继续吗?")
                .setPositiveButton("确定", (dialog, which) -> {
                    progressView.setVisibility(View.VISIBLE);
                    userCountView.setVisibility(View.VISIBLE);
                    bookCountView.setVisibility(View.VISIBLE);
                    messageView.setVisibility(View.VISIBLE);
                    messageView.setText("正在下载数据中....");
                    userCountView.setText("用户: 0");
                    bookCountView.setText("账本: 0");
                    LocalDao.clearHistoryData();
                    RemoteDao dao = new RemoteDao(getContext());
                    dao.downloadData(LocalDao.getCurrentOperator().id
                            , (bookCount, infoCount) -> {
                                getActivity().runOnUiThread(() -> {
                                    if (infoCount > 0) {
                                        userCountView.setText("用户: " + infoCount);
                                    }
                                    if (bookCount > 0) {
                                        bookCountView.setText("账本: " + bookCount);
                                    }
                                });
                            }
                            , (books, infos) -> {
                                getActivity().runOnUiThread(() -> {
                                    progressView.setVisibility(View.INVISIBLE);
                                    userCountView.setVisibility(View.VISIBLE);
                                    bookCountView.setVisibility(View.VISIBLE);
                                    messageView.setText("下载数据");
                                    userCountView.setText("用户: " + infos.size());
                                    bookCountView.setText("账本: " + books.size());
                                    doView.setText("完成");
                                    doView.setSelected(true);
                                });
                            });
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
