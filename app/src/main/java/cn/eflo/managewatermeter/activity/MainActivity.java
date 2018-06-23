package cn.eflo.managewatermeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.eflo.managewatermeter.R;
import cn.eflo.managewatermeter.dao.LocalDao;
import cn.eflo.managewatermeter.fragment.AccountBookFragment;
import cn.eflo.managewatermeter.fragment.DownloadDataFragment;
import cn.eflo.managewatermeter.fragment.SettingFragment;
import cn.eflo.managewatermeter.fragment.UploadDataFragment;
import cn.eflo.managewatermeter.model.Operator;
import cn.eflo.managewatermeter.util.CodeUtil;
import cn.eflo.managewatermeter.util.WLog;

public class MainActivity extends DefaultActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    TextView userNameView;

    private Operator operator;

    Fragment currentFragment;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        operator = LocalDao.getCurrentOperator();
        if (operator == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userNameView = navigationView.getHeaderView(0).findViewById(R.id.user_name_view);
        navigationView.getHeaderView(0).findViewById(R.id.logout_btn).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("注销")
                    .setMessage("您确定要退出该帐号吗?")
                    .setPositiveButton("确定", (dialog, i) -> {
                        dialog.dismiss();
                        LocalDao.resetOperatorsFlag();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("取消", (dialog, i) -> dialog.dismiss())
                    .show();
        });
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        userNameView.setText(operator.name);
        currentFragment = new AccountBookFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_layout, currentFragment)
                .show(currentFragment)
                .commitNowAllowingStateLoss();
        toolbar.setTitle("账本");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitApp();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(currentFragment);
        if (id == R.id.nav_accountbook) {
            toolbar.setTitle("账本");
            currentFragment = new AccountBookFragment();
        } else if (id == R.id.nav_download) {
            toolbar.setTitle("下载数据");
            currentFragment = new DownloadDataFragment();
        } else if (id == R.id.nav_upload) {
            toolbar.setTitle("上传数据");
            currentFragment = new UploadDataFragment();
        } else if (id == R.id.nav_setting) {
            toolbar.setTitle("设置");
            currentFragment = new SettingFragment();
        }
        transaction.add(R.id.fragment_layout, currentFragment);
        transaction.show(currentFragment);
        transaction.commitNowAllowingStateLoss();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            Toast.makeText(this, "再次点击退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
