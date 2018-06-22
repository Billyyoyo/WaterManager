package cn.e_flo.managewatermeter.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.e_flo.managewatermeter.R;
import cn.e_flo.managewatermeter.dao.LocalDao;
import cn.e_flo.managewatermeter.dao.RemoteDao;
import cn.e_flo.managewatermeter.event.DownloadCompleteEvent;
import cn.e_flo.managewatermeter.event.EventBus;
import cn.e_flo.managewatermeter.model.RecordInfo;

public class UploadDataFragment extends DefaultFragment {

    @BindView(R.id.progress_view)
    ProgressBar progressView;
    @BindView(R.id.message_view)
    TextView messageView;
    @BindView(R.id.info_count_view)
    TextView infoCountView;

    List<RecordInfo> infos;

    public UploadDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_data, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        progressView.setVisibility(View.INVISIBLE);
        messageView.setVisibility(View.INVISIBLE);
        infoCountView.setVisibility(View.VISIBLE);
        infos = LocalDao.getDidInfos();
        infoCountView.setText("0 / "+infos.size());
    }

    @OnClick(R.id.submit_btn)
    public void toUpload() {
        new AlertDialog.Builder(getContext())
                .setTitle("上传数据")
                .setMessage("上传数据\n您确定要继续吗?")
                .setPositiveButton("确定", (dialog, which) -> {
                    progressView.setVisibility(View.VISIBLE);
                    messageView.setVisibility(View.VISIBLE);
                    messageView.setText("正在上传数据中....");
                    infoCountView.setText("用户: 0");
                    LocalDao.clearHistoryData();
                    RemoteDao dao = new RemoteDao(getContext());
                    dao.uploadData(infos
                            , (infoCount) -> {
                                getActivity().runOnUiThread(() -> {
                                    if (infoCount > 0) {
                                        infoCountView.setText(infoCount+" / " + infos.size());
                                    }
                                });
                            }
                            , (ret) -> {
                                getActivity().runOnUiThread(() -> {
                                    progressView.setVisibility(View.INVISIBLE);
                                    infoCountView.setVisibility(View.VISIBLE);
                                    messageView.setText("上传完成");
                                    EventBus.post(new DownloadCompleteEvent());
                                });
                            });
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

}