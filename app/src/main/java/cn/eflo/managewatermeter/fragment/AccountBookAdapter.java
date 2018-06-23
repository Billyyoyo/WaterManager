package cn.e_flo.managewatermeter.fragment;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.e_flo.managewatermeter.R;
import cn.e_flo.managewatermeter.activity.InfoActivity;
import cn.e_flo.managewatermeter.model.AccountBook;

public class AccountBookAdapter extends RecyclerView.Adapter<AccountBookAdapter.ViewHolder> {

    private List<AccountBook> bookList;

    public AccountBookAdapter() {
        bookList = new ArrayList<>();
    }

    public AccountBookAdapter(List<AccountBook> list) {
        bookList = list;
    }

    public void setData(List<AccountBook> list) {
        bookList = list;
        if (list != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.accountbook_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindData(bookList.get(position));

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        @BindView(R.id.book_name_view)
        public TextView mNameView;
        @BindView(R.id.total_view)
        public TextView totalView;
        @BindView(R.id.complete_view)
        public TextView completeView;
        @BindView(R.id.undo_view)
        public TextView undoView;
        public AccountBook mBook;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }

        public void bindData(AccountBook book) {
            mBook = book;
            if (mBook.name != null) {
                mNameView.setText(mBook.name);
            }
            totalView.setText("用户总数 " + mBook.total);
            completeView.setText("已抄表户数 " + mBook.complete);
            undoView.setText("未抄表户数 " + (mBook.total - mBook.complete));
        }

        @OnClick(R.id.book_item_view)
        public void toItemClick() {
            Intent intent = new Intent(mView.getContext(), InfoActivity.class);
            intent.putExtra("BOOK_ID", mBook.id);
            mView.getContext().startActivity(intent);
        }

        @Override
        public String toString() {
            return mBook.name;
        }
    }
}
