package cn.eflo.managewatermeter.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.eflo.managewatermeter.R;
import cn.eflo.managewatermeter.dao.LocalDao;
import cn.eflo.managewatermeter.fragment.InfoFragment;
import cn.eflo.managewatermeter.model.RecordInfo;

public class InfoActivity extends DefaultActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.container)
    ViewPager mViewPager;

    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        bookId = getIntent().getStringExtra("BOOK_ID");
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        List<RecordInfo> infos = LocalDao.getRecordInfos(bookId);
        getSupportActionBar().setTitle("第[" + 1 + "/" + infos.size() + "]户");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), infos);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle("第[" + (position + 1) + "/" + infos.size() + "]户");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("搜索户号、表号或户名…");
        searchView.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) return true;
                for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                    RecordInfo info = mSectionsPagerAdapter.infoList.get(i);
                    if (query.equalsIgnoreCase(info.userId)
                            || query.equalsIgnoreCase(info.waterMeterId)
                            || query.equalsIgnoreCase(info.userName)) {
                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    @OnClick(R.id.first_btn)
    public void toFirst() {
        mViewPager.setCurrentItem(0);
    }

    @OnClick(R.id.next_btn)
    public void toNext() {
        int index = mSectionsPagerAdapter.nextUndoIndex();
        if (index > 0) {
            mViewPager.setCurrentItem(index);
        } else {

        }
    }

    @OnClick(R.id.last_btn)
    public void toLast() {
        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount() - 1);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<RecordInfo> infoList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, List<RecordInfo> infos) {
            super(fm);
            infoList = infos;
        }

        @Override
        public Fragment getItem(int position) {
            return InfoFragment.newInstance(infoList.get(position));
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        public int nextUndoIndex() {
            for (int i = mViewPager.getCurrentItem() + 1; i < infoList.size(); i++) {
                RecordInfo info = infoList.get(i);
                if (info.readFlag == RecordInfo.READFLAG_UNDO) {
                    return i;
                }
            }
            return -1;
        }
    }
}
