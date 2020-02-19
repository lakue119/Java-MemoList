package com.lakue.linememolist.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lakue.linememolist.Listener.OnItemClickListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmObject;

public class ViewHolderMemo extends MyItemView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.ll_memo)
    LinearLayout ll_memo;

    DataMemo dataMemo;

    OnItemClickListener onItemClickListener;

    public ViewHolderMemo(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void onBind(RealmObject data){
        this.dataMemo = (DataMemo)data;

        tv_title.setText(dataMemo.getTitle());
        tv_content.setText(dataMemo.getContent());
        if(dataMemo.getThumbnail() == null){
            iv_img.setVisibility(View.GONE);
        } else {
            Glide.with(itemView.getContext()).load(dataMemo.getThumbnail()).into(iv_img);
        }

        ll_memo.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onItemClickListener.onItemClick(dataMemo.getIdx());
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}