package com.lakue.linememolist.ViewHolder;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.R;

public class ViewHolderGridEditItem {

    Context context;

    Drawable bm = null;

    ImageView iv_edit;


    public ViewHolderGridEditItem(Context context, View view) {
        this.context = context;

        iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
    }

    public void onBind(String data) {
        if(bm  == null) {
            Glide.with(context)
                    .load(data)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable drawable,
                                                    @Nullable Transition<? super Drawable>
                                                            transition) {
                            bm = drawable;
                            iv_edit.setImageDrawable(drawable);

                        }
                    });

        } else {
            iv_edit.setImageDrawable(bm);
        }

        iv_edit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //onChangeFragmentListener.onFragmentChange(0,null);
            }
        });
        //mGlideRequestManager.load(data).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(iv_image);
    }

//    public void setOnChangeFragmentListener(OnChangeFragmentListener onChangeFragmentListener) {
//        this.onChangeFragmentListener = onChangeFragmentListener;
//    }

    //인원체크 버튼 정사각형으로 바꾸기~~
    private void setHeight(ImageView iv_event_img) {

        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();

        int width = dm.widthPixels;

        int content_width = (int) (width / 3)-20;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(content_width,content_width);
        iv_event_img.setLayoutParams(params);

        //iv_event_img.getLayoutParams().height = (int)(content_width * 1.5);

    }
}
