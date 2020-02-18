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

    final int PICK_FROM_ALBUM = 2001;

    Context context;

    int REQUEST_IMAGE_CAPTURE = 1111;

    private String imageFilePath;

    private Uri photoUri;
    String image = "";
    Bitmap rotatebitmap;

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
////                Intent intent = new Intent();
////                intent.putExtra("result", Common.TYPE_ALBUM);
////                setResult(RESULT_OK, intent);
////
////                //액티비티(팝업) 닫기
////                finish();
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                checkPermission(permissions, false);
            }
        });

        rl_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //데이터 전달하기
//                Intent intent = new Intent();
//                intent.putExtra("result", Common.TYPE_PHOTO);
//                setResult(RESULT_OK, intent);
//
//                //액티비티(팝업) 닫기
//                finish();
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                checkPermission(permissions, true);
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

    private void checkPermission(String[] permission, final Boolean isCamera) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(PopupActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
                if (isCamera) {
                    sendTakePhotoIntent();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(PopupActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("사진 접근 권한을 허용하지 않으면 사진을 올릴 수 없습니다.\n[설정] > [권한] 에서 권한을 허용해주세요.")
                .setPermissions(permission)
                .check();
    }

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
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

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

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public String getRealpath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        c.moveToFirst();
        String path = c.getString(index);

        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;


        if (requestCode == PICK_FROM_ALBUM) {
            Uri dataUri = data.getData();

            //image = data.getStringExtra("editimage");
            if (dataUri != null) {
                image = getRealpath(dataUri);
                //imagePathToBitmap(image);
                Log.i("AAAAAAAAAAAAAAAA", "convertImageToByte(dataUri)" + convertImageToByte(dataUri));
                //데이터 전달하기
                Intent intent = new Intent();
                intent.putExtra("result", Common.TYPE_ALBUM);
                intent.putExtra("data",convertImageToByte(dataUri));
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            new ConvertTask().execute(imageFilePath);
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

            rotatebitmap = rotate(bitmap, exifDegree);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatebitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);

            return stream.toByteArray();
        }

        //이 Task에서(즉 이 스레드에서) 수행되던 작업이 종료되었을 때 호출됨
        @Override
        protected void onPostExecute(byte[] result) {
            Intent intent = new Intent();
            intent.putExtra("result", Common.TYPE_PHOTO);
            intent.putExtra("data",result);
            setResult(RESULT_OK, intent);

            //액티비티(팝업) 닫기
            finish();
        }

    }

//    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {
//
//        @Override
//        protected Void doInBackground(byte[]... data) {
//            FileOutputStream outStream = null;
//
//
//            try {
//                SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd_HHmmss");
//                Date date = new Date();
//
//                File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest");
//                if (!path.exists()) {
//                    path.mkdirs();
//                }
//
//                String fileName = day.format(date) + ".jpg";
//                File outputFile = new File(path, fileName);
//
//                image = outputFile.getPath();
//
//                outStream = new FileOutputStream(outputFile);
//                outStream.write(data[0]);
//                outStream.flush();
//                outStream.close();
//
//                Log.d("PHOTO", "onPictureTaken - wrote bytes: " + data.length + " to "
//                        + outputFile.getAbsolutePath());
//
//
//                // 갤러리에 반영
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                mediaScanIntent.setData(Uri.fromFile(outputFile));
//                context.sendBroadcast(mediaScanIntent);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
////        @Override
////        protected void onPostExecute(Void aVoid) {
////            super.onPostExecute(aVoid);
////            showImageView();
////
////        }
//    }
//
//    public static byte[] hexStringToByteArray(String s) {
//        byte[] b = new byte[s.length() / 2];
//        for (int i = 0; i < b.length; i++) {
//            int index = i * 2;
//            int v = Integer.parseInt(s.substring(index, index + 2), 16);
//            b[i] = (byte) v;
//        }
//        return b;
//    }

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
