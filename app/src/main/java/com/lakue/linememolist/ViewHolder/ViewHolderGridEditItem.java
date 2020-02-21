package com.lakue.linememolist.ViewHolder;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lakue.linememolist.Listener.OnImageDeleteListener;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.MyItemView;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderGridEditItem extends MyItemView {

    @BindView(R.id.iv_edit)
    ImageView iv_edit;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;

    private Context context;

    private OnImageDeleteListener onImageDeleteListener;
    private OnImageInsertListener onImageInsertListener;

    public ViewHolderGridEditItem(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    //이미지가 있는 경우, 경로에서 가져온 이미지를 보여줌
    public void onBind(Object data, int position) {

        Glide.with(context).load(data).into(iv_edit);

        iv_edit.setEnabled(false);
        iv_delete.setVisibility(View.VISIBLE);
        iv_delete.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onImageDeleteListener.onImageDelete(position);
            }
        });
        setImageHeight(false);
    }

    //이미지 추가기능 버튼을 보여줌
    public void onBind() {
        iv_edit.setEnabled(true);
        Glide.with(context).load(R.drawable.b_circleplus).into(iv_edit);
        iv_delete.setVisibility(View.GONE);
        iv_edit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onImageInsertListener.onImageInsert();
            }
        });
        setImageHeight(true);

    }


    //화면 크기에 따라 이미지 크기 정사각비율을 지정
    private void setImageHeight(Boolean isAddItem) {

        DisplayMetrics dm = itemView.getResources().getDisplayMetrics();

        int width = dm.widthPixels; //현재 디바이스의 가로길이

        int content_width = (int) ((width - 300) / 3);

        int _10px = Common.convertPixelsToDp(10, itemView.getContext());
        int _20px = Common.convertPixelsToDp(20, itemView.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(content_width, content_width);
        params.setMargins(_10px, _10px, _10px, _10px);
        iv_edit.setLayoutParams(params);
        iv_edit.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //추가버튼의 이미지를 작게 보여주기 위해 padding값을 부여
        if (isAddItem) {
            iv_edit.setPadding(_20px, _20px, _20px, _20px);
        } else {
            iv_edit.setPadding(0, 0, 0, 0);
        }

    }

    //이벤트 리스너
    public void setOnImageDeleteListener(OnImageDeleteListener onImageDeleteListener) {
        this.onImageDeleteListener = onImageDeleteListener;
    }

    public void setOnImageInsertListener(OnImageInsertListener onImageInsertListener) {
        this.onImageInsertListener = onImageInsertListener;
    }
}
