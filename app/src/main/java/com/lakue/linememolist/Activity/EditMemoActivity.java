package com.lakue.linememolist.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lakue.linememolist.Adapter.AdapterGrid;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Module.CameraImage;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.PopupActivity;
import com.lakue.linememolist.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class EditMemoActivity extends AppCompatActivity {

    //    @BindView(R.id.gv_memo_item)
//    ExpandableHeightGridView gv_memo_item;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.btn_memo_insert)
    Button btn_memo_insert;
    @BindView(R.id.rv_memo_item)
    RecyclerView rv_memo_item;

    AdapterGrid adapter;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        ButterKnife.bind(this, this);

        init();

        setData();

        btn_memo_insert.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //Realm에 작성한 메모 저장
                addMemo();
            }
        });

    }

    private void init() {
        //recyclerview 초기화
        GridLayoutManager mGridLayoutManager;

        int numberOfColumns = 3; // 한줄에 5개의 컬럼을 추가합니다.
        mGridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        rv_memo_item.setLayoutManager(mGridLayoutManager);

        adapter = new AdapterGrid();
        rv_memo_item.setAdapter(adapter);


    }


    //이미지 저장
    private void setData() {
//        String item;
//        for (int i = 0; i < 1; i++) {
//
//            if (i % 2 == 0) {
//                item = "https://cdn.crewbi.com/images/goods_img/20180906/301986/301986_a_500.jpg?v=1536196415";
//            } else {
//                item = "https://seoul-p-studio.bunjang.net/product/60478633_1_1479473294_w640.jpg";
//            }
//
//            adapter.addItem(item);
//        }

        adapter.setOnImageInsertListener(new OnImageInsertListener() {
            @Override
            public void onImageInsert() {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                startActivityForResult(intent, Common.REQUEST_IMAGE_TYPE);
            }
        });
    }

    //Realm에 객체(데이터) 저장
    private void addMemo() {

        //기본키인 idx를 고유값으로 지정
        Number number = realm.where(DataMemo.class).max("idx");
        long memoId = number == null ? 0 : number.longValue() + 1;

        final DataMemo memo = new DataMemo(memoId, et_title.getText().toString(), et_content.getText().toString());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Realm에 생성한 메모를 저장
                realm.copyToRealm(memo);

                //데이터 저장 후 MainActivity에 완료를 알림
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    //Realm 초기화
    private void initRealm() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }
    CameraImage cameraImage = new CameraImage(this);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            //갤러리에서 가져올건지.. 사진촬영으로 가져올건지.. URL을 통해 가져올건지를 선택한 결과값
            case Common.REQUEST_IMAGE_TYPE:
//                if (data.getExtras().getInt("result", 0) == Common.TYPE_ALBUM ||
//                        data.getExtras().getInt("result", 0) == Common.TYPE_PHOTO) {
//                    byte[] img = data.getExtras().getByteArray("data");
//                    adapter.addItem(img);
//                }
                if(data.getExtras().getInt("result",0) == Common.TYPE_ALBUM){
                    //앨범으로 이동
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, Common.REQUEST_ALBUM);
                } else if(data.getExtras().getInt("result",0) == Common.TYPE_PHOTO){
                    //사진촬영으로 이동
                    cameraImage.showCamera();

                } else if(data.getExtras().getInt("result",0) == Common.TYPE_URL){
                    //링크 설정으로 이동

                }
                break;

                //앨범에서 선택한 이미지를 가져옴.
            case Common.REQUEST_ALBUM:
                Uri dataUri = data.getData();

                if (dataUri != null) {
                    adapter.addItem(convertImageToByte(dataUri));
                }

                break;
                //사진촬영으로 가져온 이미지
            case Common.REQUEST_IMAGE_CAPTURE:
                Log.i("AWERAWER",cameraImage.getImageFilePath());
                new ConvertTask().execute(cameraImage.getImageFilePath());
                break;

        }
    }

    private class ConvertTask extends AsyncTask<String,Void,byte[]> {

        @Override
        protected byte[] doInBackground(String... strings) {
            Bitmap bitmap = BitmapFactory.decodeFile(strings[0]);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            Bitmap rotatebitmap = rotate(bitmap, exifDegree);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatebitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);

            return stream.toByteArray();
        }

        //이 Task에서(즉 이 스레드에서) 수행되던 작업이 종료되었을 때 호출됨
        @Override
        protected void onPostExecute(byte[] result) {
            adapter.addItem(result);
//            Intent intent = new Intent();
//            intent.putExtra("result", Common.TYPE_PHOTO);
//            intent.putExtra("data",result);
//            setResult(RESULT_OK, intent);
//
//            //액티비티(팝업) 닫기
//            finish();
        }
    }


    //가져온 이미지가 회전되어있을 경우 실제 각도 찾아내기
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // 이미지 회전
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //이미지의 절대경로 가져오기
    public String getRealpath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        c.moveToFirst();
        String path = c.getString(index);

        return path;
    }

    //Uri타입을 byte[]형식으로 변환
    public byte[] convertImageToByte(Uri uri) {
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}
