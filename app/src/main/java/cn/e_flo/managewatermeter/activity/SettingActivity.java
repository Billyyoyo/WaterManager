package cn.e_flo.managewatermeter.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import butterknife.ButterKnife;
import cn.e_flo.managewatermeter.R;
import cn.e_flo.managewatermeter.fragment.SettingFragment;

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
