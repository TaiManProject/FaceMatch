package com.facematch;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class SelectPicActivity extends Activity implements OnClickListener {

    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    public static final String KEY_PHOTO_PATH = "photo_path";
    private static final String TAG = "SelectPicActivity";
    private static String photoPath = "/sdcard/MyPic/";
    private LinearLayout dialogLayout;
    private Button takePhotoBtn;
    private Button pickPhotoBtn;
    private Button cancelBtn;
    private String picPath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pic_layout);

        dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
        dialogLayout.setOnClickListener(this);
        takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
        pickPhotoBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.dialog_layout:
                finish();
                break;
            case R.id.btn_take_photo:
                takePhoto();
                break;
            case R.id.btn_pick_photo:
                pickPhoto();
                break;
            default:
                finish();
                break;
        }

    }

    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */

			/*
             * ContentValues values = new ContentValues(); photoUri =
			 * this.getContentResolver
			 * ().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			 * intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
			 * photoUri); intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			 */
            // 初始化
            picPath = null;
            // CramerProActivity.imageView.setImageBitmap(null);
            String name = new DateFormat().format("yyyyMMdd_hhmmss",
                    Calendar.getInstance(Locale.CHINA))
                    + ".jpg";
            File file = new File(photoPath);
            if (!file.exists()) {
                // 检查图片存放的文件夹是否存在
                file.mkdir();
                // 不存在的话 创建文件夹
            }
            picPath = photoPath + name;
            File photo = new File(picPath);
            photoUri = Uri.fromFile(photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); // 这样就将文件的存储方式和uri指定到了Camera应用中
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data == null) {
                Toast.makeText(this, "1", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "2", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        Log.i(TAG, "imagePath = " + picPath);
        /*if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))*/
        if (picPath != null) {
            Intent intent = new Intent();
            intent.setClass(SelectPicActivity.this, ResultPage.class);
            intent.putExtra(KEY_PHOTO_PATH, picPath);
            intent.putExtra("ClassName", "SelectPicActivity");
            intent.putExtra("MethodName", "takePhoto");
            startActivity(intent);
            finish();
//
//			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
//			setResult(Activity.RESULT_OK, lastIntent);
//			finish();
        } else {
            Toast.makeText(this, "3", Toast.LENGTH_LONG).show();

        }
    }
}
