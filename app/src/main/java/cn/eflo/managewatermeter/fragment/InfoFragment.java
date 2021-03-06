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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.eflo.managewatermeter.R;
import cn.eflo.managewatermeter.activity.InfoActivity;
import cn.eflo.managewatermeter.dao.LocalDao;
import cn.eflo.managewatermeter.model.AccountBook;
import cn.eflo.managewatermeter.model.RecordInfo;
import cn.eflo.managewatermeter.util.DateUtil;
import cn.eflo.managewatermeter.util.SystemUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private RecordInfo info;

    @BindView(R.id.UserIDTextView)
    TextView userIdView;
    @BindView(R.id.MeterIDTextView)
    TextView meterIdView;
    @BindView(R.id.UserNameTextView)
    TextView userNameView;
    @BindView(R.id.AddressTextView)
    TextView userAddressView;
    @BindView(R.id.LastReadTextView)
    TextView lastReadView;
    @BindView(R.id.LastNumTextView)
    TextView lastNumView;
    @BindView(R.id.NewReadEditText)
    EditText thisReadView;
    @BindView(R.id.ThisNumtextView)
    TextView thisNumView;
    @BindView(R.id.CaliberTextView)
    TextView caliberView;
    @BindView(R.id.StatusTextView)
    TextView statusView;
    @BindView(R.id.ReadFlagTextView)
    TextView readFlagView;
    @BindView(R.id.WaterTypeTextView)
    TextView waterTypeView;
    @BindView(R.id.BookNameTextView)
    TextView bookNameView;

    private View contentView;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(RecordInfo info) {
        InfoFragment fragment = new InfoFragment();
        fragment.info = info;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, contentView);
        contentView.setOnClickListener(v -> SystemUtil.hideSoftKeyboard(thisReadView));
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        userIdView.setText(info.userId);
        meterIdView.setText(info.waterMeterId);
        userNameView.setText(info.userName);
        userAddressView.setText(info.userAddress);
        lastReadView.setText("" + info.lastMonthNum);
        lastNumView.setText("" + info.lastMonthUse);
        if (info.readFlag != RecordInfo.READFLAG_UNDO) {
            thisReadView.setText("" + info.thisMonthNum);
        }
        if (info.thisMonthNum > 0) {
            thisNumView.setText("" + info.thisMonthUse);
        }
        caliberView.setText("" + info.waterMeterFlag);
        statusView.setText(RecordInfo.STATUSFLAGS[info.status]);
        readFlagView.setText(RecordInfo.READFLAGS[info.readFlag]);
        waterTypeView.setText(info.useType);
        bookNameView.setText(info.accountBookName);
    }

    @OnClick(R.id.save_button)
    public void toSave() {
        if (TextUtils.isEmpty(thisReadView.getText().toString())) {
            Snackbar.make(thisReadView, "请输入正确的读数", Snackbar.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage("是否保存数据")
                    .setPositiveButton("确定", (dialog, which) -> {
                        info.thisMonthNum = Integer.parseInt(thisReadView.getText().toString());
                        info.thisMonthUse = (info.thisMonthNum > info.lastMonthNum) ? info.thisMonthNum - info.lastMonthNum : 0;
                        info.readFlag = RecordInfo.READFLAG_DID;
                        info.readDate = DateUtil.getToday();
                        info.save();
                        readFlagView.setText(RecordInfo.READFLAGS[RecordInfo.READFLAG_DID]);
                        AccountBook book = LocalDao.getAccountBook(info.accountBookId);
                        book.complete = book.complete + 1;
                        book.save();
                        thisNumView.setText("" + (info.thisMonthNum - info.lastMonthNum));
                    })
                    .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

}
