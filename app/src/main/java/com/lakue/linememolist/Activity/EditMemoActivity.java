package com.lakue.linememolist.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.linememolist.Adapter.AdapterGrid;
import com.lakue.linememolist.Listener.OnImageDeleteListener;
import com.lakue.linememolist.Listener.OnImageInsertListener;
import com.lakue.linememolist.Listener.OnSingleClickListener;
import com.lakue.linememolist.Model.DataMemo;
import com.lakue.linememolist.Model.DataMemoImg;
import com.lakue.linememolist.Module.CameraImageModule;
import com.lakue.linememolist.Module.Common;
import com.lakue.linememolist.Module.ModuleActivity;
import com.lakue.linememolist.R;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class EditMemoActivity extends ModuleActivity {

    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.btn_memo_insert)
    Button btn_memo_insert;
    @BindView(R.id.rv_memo_item)
    RecyclerView rv_memo_item;

    private AdapterGrid adapter;

    private Realm realm;

    private CameraImageModule cameraImageModule;

    private Common common;

    private Boolean isUpdate = false;
    private long memo_idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        ButterKnife.bind(this, this);
        common = new Common(this);

        init();
        checkType();


        btn_memo_insert.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //Realm에 작성한 메모 저장
                if(!isDataEmpty()){
                    if(isUpdate){
                        updateMemo();
                    } else{
                        addMemo();
                    }
                }
            }
        });
    }



    //글 Insert폼인지, Update폼인지 판단
    private void checkType(){
        Intent intent = getIntent();
        if(intent != null){
            int type = getIntent().getExtras().getInt("type");
            if(type == Common.TYPE_INTENT_UPDATE){
                memo_idx = getIntent().getExtras().getLong("memo_idx");
                isUpdate = true;
                getResultMemoList();
                getResultMemoImgs();
                btn_memo_insert.setText("수정");
            }

        }
    }

    //제목,내용을 모두 입력했는지 확인
    private Boolean isDataEmpty(){
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();

        if(title.isEmpty()){
            common.showToast("제목을 입력해주세요.");
            return true;
        }
        if(content.isEmpty()){
            common.showToast("내용을 입력해주세요.");
            return true;
        }
        return false;
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

    //region Realm접근
    //Realm에 객체(데이터) 저장
    private void addMemo() {

        //기본키인 idx를 고유값으로 지정
        Number maxMemoNum = realm.where(DataMemo.class).max("idx");
        long memoId = maxMemoNum == null ? 0 : maxMemoNum.longValue() + 1;

        final DataMemo memo = new DataMemo(memoId, et_title.getText().toString(), et_content.getText().toString(),adapter.getImage(0));

        realm.executeTransaction(realm -> {
            //Realm에 생성한 메모를 저장
            realm.copyToRealm(memo);   //Realm에 생성한 메모를 저장
            addMemoImg(memoId);

            common.showToast("메모가 작성되었습니다.");
            //데이터 저장 후 MainActivity에 완료를 알림
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    //메모 수정
    private void updateMemo(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // 쿼리를 해서 하나를 가져온다.
                DataMemo dataMemo = realm.where(DataMemo.class)
                        .equalTo("idx", memo_idx)
                        .findFirst();

                dataMemo.setTitle(et_title.getText().toString());
                dataMemo.setContent(et_content.getText().toString());
                dataMemo.setThumbnail(adapter.getImage(0));

                RealmResults<DataMemoImg> realmResultImgs = realm.where(DataMemoImg.class).equalTo("memo_idx", memo_idx).findAll();
                realmResultImgs.deleteAllFromRealm();

                addMemoImg(memo_idx);

                common.showToast("메모가 수정되었습니다.");

                //데이터 수정 후 MainActivity에 완료를 알림
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    //데이터베이스에 메모 이미지 저장
    private void addMemoImg(long memoId){
        Number maxMemoImgNum = realm.where(DataMemoImg.class).max("img_idx");
        long memoImgId = maxMemoImgNum == null ? 0 : maxMemoImgNum.longValue() + 1;

        for(int i=0;i<adapter.getItemCount()-1;i++){
            realm.copyToRealm(new DataMemoImg(memoImgId+i, memoId, adapter.getImage(i)));
        }

    }

    //글 수정 폼이면, 메모리스트 데이터 가져오기
    private void getResultMemoList() {
        RealmResults<DataMemo> realmResults = realm.where(DataMemo.class)
                .equalTo("idx", memo_idx)
                .findAllAsync();

        et_title.setText(realmResults.get(0).getTitle());
        et_content.setText(realmResults.get(0).getContent());

    }
    //글 수정 폼이면, 메모이미리스트 데이터 가져오기
    private void getResultMemoImgs() {
        RealmResults<DataMemoImg> realmResultImgs = realm.where(DataMemoImg.class)
                .equalTo("memo_idx", memo_idx)
                .findAllAsync();

        for (DataMemoImg memoImg : realmResultImgs) {
            common.printLog("memoImg" + memoImg.toString());
            DataMemoImg data = new DataMemoImg(memoImg.getImg_idx(), memoImg.getMemo_idx(), memoImg.getImg_file());
            adapter.addItem(data.getImg_file());
        }
    }

    //endregion

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
                    if(cameraImageModule == null){
                        cameraImageModule = new CameraImageModule(this);
                    }
                    cameraImageModule.showCameraAlbum();

                } else if (data.getExtras().getInt("result", 0) == Common.TYPE_PHOTO) {
                    //사진촬영으로 이동
                    if(cameraImageModule == null){
                        cameraImageModule = new CameraImageModule(this);
                    }
                    cameraImageModule.showCamera();

                } else if (data.getExtras().getInt("result", 0) == Common.TYPE_URL) {
                    //링크 설정으로 이동
                    showDialog();
                }
                break;

            //앨범에서 선택한 이미지를 가져옴.
            case Common.REQUEST_ALBUM:
                Uri dataUri = data.getData();

                if (dataUri != null) {
                    adapter.addItem(cameraImageModule.sendPicture(dataUri));
                }

                break;
            //사진촬영으로 가져온 이미지
            case Common.REQUEST_IMAGE_CAPTURE:
                common.printLog(cameraImageModule.getImageFilePath());
                new ConvertTask().execute(cameraImageModule.getImageFilePath());
                break;

        }
    }

    //region Image생성 및 변환

    //URL입력 다이얼로그 생성
    private void showDialog() {
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
                is = url.openStream ();
                imageBytes = IOUtils.toByteArray(is);

                if(!isImage(imageBytes)){
                    return null;
                }

                return imageBytes;
            } catch (Exception e) {
                //http식으로 안형들어왔을 경우 null을 리턴
                return null;
            }
        }

        @Override
        protected void onPostExecute(byte[] image) {
            if(image != null){
                adapter.addItem(image);
            } else {
                common.showToast("정상적인 이미지 URL을 입력해주세요.");
            }
        }
    }

    private Boolean isImage(byte[] data){
        Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (img == null) {
            //이미지가 아닌 경우
            return false;
        }
        else {
            //이미지인경우
            return true;
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
                exifDegree = cameraImageModule.exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            Bitmap rotatebitmap = cameraImageModule.rotate(bitmap, exifDegree);

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

    //endregion

}
