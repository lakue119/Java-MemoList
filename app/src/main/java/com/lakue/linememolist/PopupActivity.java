package com.lakue.linememolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Module.Common;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopupActivity extends Activity {
    @BindView(R.id.rl_album)
    RelativeLayout rl_album;
    @BindView(R.id.rl_photo)
    RelativeLayout rl_photo;
    @BindView(R.id.rl_link)
    RelativeLayout rl_link;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        ButterKnife.bind(this, this);
        context = this;
        rl_album.setOnClickListener(v -> {
            //앨번 선택
            Intent intent = new Intent();
            intent.putExtra("result", Common.TYPE_ALBUM);
            setResult(RESULT_OK, intent);

            finish();
        });

        rl_photo.setOnClickListener(v -> {
            //사진촬영 선택
            Intent intent = new Intent();
            intent.putExtra("result", Common.TYPE_PHOTO);
            setResult(RESULT_OK, intent);

            finish();
        });

        rl_link.setOnClickListener(v -> {
            //링ㄹ크 선택
            Intent intent = new Intent();
            intent.putExtra("result", Common.TYPE_URL);
            setResult(RESULT_OK, intent);

            finish();
        });

        iv_close.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //취소 선택
                Intent intent = new Intent();
                intent.putExtra("result", Common.TYPE_CANCEL);
                setResult(RESULT_CANCELED, intent);

                finish();
            }
        });
    }
}
