package com.lakue.linememolist.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.lakue.linememolist.Adapter.AdapterGrid;
import com.lakue.linememolist.Listener.OnImageDeleteListener;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.DataMemoImg;
import com.lakue.linememolist.Module.CameraImage;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.PopupActivity;
import com.lakue.linememolist.R;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class EditMemoActivity extends AppCompatActivity {

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

        int numberOfColumns = 3; // 한줄에 3개의 컬럼을 추가합니다.
        mGridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        rv_memo_item.setLayoutManager(mGridLayoutManager);

        adapter = new AdapterGrid();
        rv_memo_item.setAdapter(adapter);

        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }


    //이미지 저장
    private void setData() {

        adapter.setOnImageInsertListener(new OnImageInsertListener() {
            @Override
            public void onImageInsert() {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                startActivityForResult(intent, Common.REQUEST_IMAGE_TYPE);
            }
        });
        adapter.setOnImageDeleteListener(new OnImageDeleteListener() {
            @Override
            public void onImageDelete(int position) {
                adapter.removeItem(position);
            }
        });
    }

    //Realm에 객체(데이터) 저장
    private void addMemo() {

        //기본키인 idx를 고유값으로 지정
        Number maxMemoNum = realm.where(DataMemo.class).max("idx");
        Number maxMemoImgNum = realm.where(DataMemoImg.class).max("img_idx");
        long memoId = maxMemoNum == null ? 0 : maxMemoNum.longValue() + 1;
        long memoImgId = maxMemoImgNum == null ? 0 : maxMemoImgNum.longValue() + 1;

        final DataMemo memo = new DataMemo(memoId, et_title.getText().toString(), et_content.getText().toString(),adapter.getImage(0));

        // 트랜잭션을 통해 데이터를 영속화합니다
        realm.beginTransaction();
        realm.copyToRealm(memo);   //Realm에 생성한 메모를 저장
        for(int i=0;i<adapter.getItemCount()-1;i++){
            realm.copyToRealm(new DataMemoImg(memoImgId+i, memoId, adapter.getImage(i)));
        }
        realm.commitTransaction();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Realm에 생성한 메모를 저장
                //realm.copyToRealm(memo);

                //데이터 저장 후 MainActivity에 완료를 알림
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    void showDialog() {
        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URL 링크 입력");
        builder.setMessage("이미지 URL을 입력해주세요.");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String link = edittext.getText().toString();
                        new NetworkTask().execute(link);

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
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
                if (data.getExtras().getInt("result", 0) == Common.TYPE_ALBUM) {
                    //앨범으로 이동
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, Common.REQUEST_ALBUM);
                } else if (data.getExtras().getInt("result", 0) == Common.TYPE_PHOTO) {
                    //사진촬영으로 이동
                    cameraImage.showCamera();

                } else if (data.getExtras().getInt("result", 0) == Common.TYPE_URL) {
                    //링크 설정으로 이동
                    showDialog();
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
                Log.i("AWERAWER", cameraImage.getImageFilePath());
                new ConvertTask().execute(cameraImage.getImageFilePath());
                break;

        }
    }

    //네트워크에서 이미지URL을 판단하고 byte[]타입으로 변환하는 쓰레드
    private class NetworkTask extends AsyncTask<String, Void, byte[]>{
        String link;
        @Override
        protected byte[] doInBackground(String... links) {
            InputStream is = null;
            byte[] imageBytes = null;
            try {
                link = links[0];
                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection exitCode = (HttpURLConnection) con;

                //200 Success가 떨어지면 byte[]로 변환
                if(exitCode.getResponseCode() == 200){
                    is = url.openStream ();
                    imageBytes = IOUtils.toByteArray(is);
                } else{
                    Toast.makeText(EditMemoActivity.this, "정상적인 URL을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                return imageBytes;
            } catch (IOException e) {
                Toast.makeText(EditMemoActivity.this, "정상적인 URL을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(byte[] image) {
            if(image != null){
                adapter.addItem(image);
            } else {
                Toast.makeText(EditMemoActivity.this, "이미지가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //촬영한 이미지를 변환하는 쓰레드
    private class ConvertTask extends AsyncTask<String, Void, byte[]> {

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
            rotatebitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            return stream.toByteArray();
        }

        //이 Task에서(즉 이 스레드에서) 수행되던 작업이 종료되었을 때 호출됨
        @Override
        protected void onPostExecute(byte[] result) {
            adapter.addItem(result);
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

    //Uri타입을 byte[]형식으로 변환
    public byte[] convertImageToByte(Uri uri) {
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}
