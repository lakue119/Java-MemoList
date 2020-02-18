package com.lakue.linememolist.Module;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.lakue.linememolist.PopupActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraImage {

    Context context;
    private String imageFilePath;

    private Uri photoUri;

    public CameraImage(Context context){
        this.context = context;
    }

    public void showCamera(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        checkPermission(permissions, true);
    }

    //권한 체크
    private void checkPermission(String[] permission, final Boolean isCamera) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(context, "권한 허가", Toast.LENGTH_SHORT).show();
                sendTakePhotoIntent();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("사진 접근 권한을 허용하지 않으면 사진을 올릴 수 없습니다.\n[설정] > [권한] 에서 권한을 허용해주세요.")
                .setPermissions(permission)
                .check();
    }

    //사진촬영 페이지로 이동
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                ((Activity)context).startActivityForResult(takePictureIntent, Common.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //촬영한 사진을 저장할 file경로와 이름 생성
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public String getImageFilePath(){
        return imageFilePath;
    }

}
