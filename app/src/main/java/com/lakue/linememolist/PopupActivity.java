package com.lakue.linememolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Module.Common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        rl_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //데이터 전달하기
                Intent intent = new Intent();
                intent.putExtra("result", Common.TYPE_ALBUM);
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        });

        rl_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //데이터 전달하기
                Intent intent = new Intent();
                intent.putExtra("result", Common.TYPE_PHOTO);
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        });

        rl_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                Intent intent = new Intent();
                intent.putExtra("result", Common.TYPE_URL);
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        });

        iv_close.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", Common.TYPE_CANCEL);
                setResult(RESULT_CANCELED, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        });
    }
}
