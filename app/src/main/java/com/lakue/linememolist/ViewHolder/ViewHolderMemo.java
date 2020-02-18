package com.lakue.linememolist.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderMemo extends MyItemView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.iv_img)
    ImageView iv_img;


    public ViewHolderMemo(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    public void onBind(DataMemo data){
        tv_title.setText(data.getTitle());
        tv_content.setText(data.getContent());
        Glide.with(itemView.getContext()).load(data.getThumbnail()).into(iv_img);
    }
}