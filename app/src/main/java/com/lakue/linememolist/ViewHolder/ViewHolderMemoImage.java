package com.lakue.linememolist.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.DataMemoImg;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.R;
import com.lakue.linememolist.View.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmObject;

public class ViewHolderMemoImage  extends MyItemView {

    @BindView(R.id.riv_memoitem)
    RoundImageView riv_memoitem;

    DataMemoImg dataMemoImg;

    public ViewHolderMemoImage(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void onBind(RealmObject data) {
        dataMemoImg = (DataMemoImg)data;
        Glide.with(itemView.getContext()).load(dataMemoImg.getImg_file()).into(riv_memoitem);
    }
}
