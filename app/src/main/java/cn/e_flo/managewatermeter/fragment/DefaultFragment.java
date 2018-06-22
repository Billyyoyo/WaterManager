package cn.e_flo.managewatermeter.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class DefaultFragment extends Fragment {
    public ProgressDialog progressDialog;

    public void showProgress() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
    }

    public void showProgress(String message) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
