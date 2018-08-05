package cn.eflo.managewatermeter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;

import com.activeandroid.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.eflo.managewatermeter.R;
import cn.eflo.managewatermeter.dao.LocalDao;
import cn.eflo.managewatermeter.dao.RemoteDao;
import cn.eflo.managewatermeter.model.Operator;
import cn.eflo.managewatermeter.util.CodeUtil;
import cn.eflo.managewatermeter.util.Constant;
import cn.eflo.managewatermeter.util.SystemUtil;
import cn.eflo.managewatermeter.util.WLog;

public class LoginActivity extends DefaultActivity {

    @BindView(R.id.UserNameEditText)
    EditText usernameBox;
    @BindView(R.id.PasswordEditText)
    EditText passwordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SystemUtil.requestInitPermission(this);
        }
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        usernameBox.setText(readLastLoginUser());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.INTENT_REQUEST_INIT_PERMISSION) {
            SystemUtil.requestInitPermission(this);
        }
    }

    @OnClick(R.id.LoginButton)
    public void toLogin() {
        if (TextUtils.isEmpty(usernameBox.getText().toString())) {
            new AlertDialog.Builder(this).setTitle("输入错误").setMessage("请输入登录名").show();
            throw new RuntimeException("adsfadsfa");
//            return;
        }
        if (TextUtils.isEmpty(passwordBox.getText().toString())) {
            new AlertDialog.Builder(this).setTitle("输入错误").setMessage("请输入登录密码").show();
            throw new RuntimeException("adsfadsfa");
//            return;
        }
        RemoteDao dao = new RemoteDao(this);
        showProgress("登录中......");
        dao.getOperator(usernameBox.getText().toString(), operator -> {
            runOnUiThread(() -> {
                dismissProgress();
                if (operator != null && passwordBox.getText().toString().equals(CodeUtil.decode(operator.password))) {
                    LocalDao.resetOperatorsFlag();
                    operator.flag = Operator.FLAG_LOGGED;
                    operator.save();
                    cacheLastLoginUser(usernameBox.getText().toString());
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    passwordBox.setText("");
                }
            });
        });

    }

    private void cacheLastLoginUser(String loginName) {
        SharedPreferences settings = getSharedPreferences("db_params", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("LastLoginUser", loginName);
        editor.commit();
    }

    private String readLastLoginUser() {
        SharedPreferences settings = getSharedPreferences("db_params", Context.MODE_PRIVATE);
        return settings.getString("LastLoginUser", "");
    }

    @OnClick(R.id.SettingButton)
    public void toSetting() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
