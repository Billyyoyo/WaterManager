package cn.eflo.managewatermeter.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import butterknife.ButterKnife;
import cn.eflo.managewatermeter.R;
import cn.eflo.managewatermeter.fragment.SettingFragment;

public class SettingActivity extends DefaultActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Fragment fragment = new SettingFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_layout, fragment)
                .show(fragment)
                .commitNowAllowingStateLoss();
    }


}
