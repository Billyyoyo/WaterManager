package cn.eflo.managewatermeter.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.concurrent.CompletableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.eflo.managewatermeter.R;
import cn.eflo.managewatermeter.dao.RemoteDao;
import cn.eflo.managewatermeter.model.RemoteDBParams;
import cn.eflo.managewatermeter.util.SystemUtil;

public class SettingFragment extends DefaultFragment {

    @BindView(R.id.DBNameEditText)
    EditText dbnameBox;
    @BindView(R.id.DBUserEditText)
    EditText rootBox;
    @BindView(R.id.DBPasswordEditText)
    EditText passwordBox;
    @BindView(R.id.IPEditText)
    EditText addressBox;
    @BindView(R.id.PortNumeditText)
    EditText portBox;

    RemoteDBParams params;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle){
        RemoteDao remoteDao = new RemoteDao(getContext());
        params = remoteDao.getDBParams();
        dbnameBox.setText(params.dbName);
        addressBox.setText(params.address);
        portBox.setText(params.port);
        rootBox.setText(params.root);
        passwordBox.setText(params.password);
    }

    @OnClick(R.id.SaveSettingButton)
    public void testAndSave() {
        if (!validate()) return;
        SystemUtil.hideSoftKeyboard(portBox);
        SystemUtil.hideSoftKeyboard(dbnameBox);
        SystemUtil.hideSoftKeyboard(passwordBox);
        SystemUtil.hideSoftKeyboard(rootBox);
        SystemUtil.hideSoftKeyboard(addressBox);
        SystemUtil.hideSoftKeyboard(portBox);
        RemoteDao remoteDao = new RemoteDao(getContext());
        params.dbName = dbnameBox.getText().toString();
        params.address = addressBox.getText().toString();
        params.port = portBox.getText().toString();
        params.root = rootBox.getText().toString();
        params.password = passwordBox.getText().toString();
        remoteDao.saveDBParams(params);
        showProgress("连接中...");
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(()->{
            if (remoteDao.connect()) {
                remoteDao.disconnect();
                return true;
            }
            return false;
        });
        future.thenAccept(ret->{
            getActivity().runOnUiThread(()->{
                dismissProgress();
                if (ret) {
                    Snackbar.make(portBox, "连接成功", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(portBox, "连接失败", Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean validate() {
        String message = "";
        if (TextUtils.isEmpty(dbnameBox.getText())) {
            message += "数据库名必须填写\n";
        }
        if (TextUtils.isEmpty(addressBox.getText())) {
            message += "数据库地址必须填写\n";
        }
        if (TextUtils.isEmpty(portBox.getText())) {
            message += "数据库端口必须填写\n";
        }
        if (TextUtils.isEmpty(rootBox.getText())) {
            message += "用户名必须填写\n";
        }
        if (TextUtils.isEmpty(dbnameBox.getText())) {
            message += "密码必须填写\n";
        }
        if (TextUtils.isEmpty(message)) {
            return true;
        } else {
            new AlertDialog.Builder(getContext()).setTitle("输入错误").setMessage(message).show();
            return false;
        }
    }

}
