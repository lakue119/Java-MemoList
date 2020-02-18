package com.lakue.linememolist.ViewHolder;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lakue.linememolist.Listener.OnImageDeleteListener;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.MyItem;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderGridEditItem extends MyItemView {

    @BindView(R.id.iv_edit)
    ImageView iv_edit;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;

    Context context;

    OnImageDeleteListener onImageDeleteListener;
    OnImageInsertListener onImageInsertListener;

    int position;

    public ViewHolderGridEditItem(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        context = itemView.getContext();
    }

    public void onBind(Object data,int position) {
        this.position = position;

        Glide.with(context).load(data).into(iv_edit);

        iv_edit.setEnabled(false);
        iv_delete.setVisibility(View.VISIBLE);
        iv_delete.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onImageDeleteListener.onImageDelete(position);
            }
        });
    }

    public void onBind(int position){
        this.position = position;
        iv_edit.setEnabled(true);
        Glide.with(context).load(R.drawable.fp_b_circleplus).into(iv_edit);
        iv_delete.setVisibility(View.GONE);
        iv_edit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onImageInsertListener.onImageInsert();
            }
        });

    }

    public void setOnImageDeleteListener(OnImageDeleteListener onImageDeleteListener) {
        this.onImageDeleteListener = onImageDeleteListener;
    }

    public void setOnImageInsertListener(OnImageInsertListener onImageInsertListener) {
        this.onImageInsertListener = onImageInsertListener;
    }
}
