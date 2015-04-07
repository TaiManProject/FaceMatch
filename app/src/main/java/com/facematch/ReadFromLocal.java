package com.facematch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ReadFromLocal extends Activity {
    public static final String KEY_PHOTO_PATH = "photo_path";
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    // 图片缩放倍率（相对屏幕尺寸的缩小倍率）
    public static final int SCALE_FACTOR = 8;
    static final int REQUEST_CODE = 1;
    private final String TAG = "ReadFromLocal";
    // 图片间距（控制各图片之间的距离）
    private final int GALLERY_SPACING = -10;
    private Uri photoUri;
    private String picPath;
    private boolean isSketch = false;
    private Context mContext;
    // 控件
    private GalleryFlow mGalleryFlow;
    private int selectedPictureId;

    private ImageButton camera;
    private Button sketchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.read_from_local);

        camera = (ImageButton) findViewById(R.id.camera);
        sketchButton = (Button) findViewById(R.id.sketchButton);

        initGallery();

        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent1 = new Intent(ReadFromLocal.this,
                        SelectPicActivity.class);
//				startActivityForResult(intent1, TO_SELECT_PHOTO);
                startActivity(intent1);

            }
        });

        sketchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isSketch = true;
                pickPhoto();
            }
        });

    }

    private void initGallery() {
        // 图片ID
        final int[] images = {R.drawable.picture_1, R.drawable.picture_2,
                R.drawable.picture_3, R.drawable.picture_4,
                R.drawable.picture_5, R.drawable.picture_6,
                R.drawable.picture_7};
//                R.drawable.picture_8,
//                R.drawable.picture_9, R.drawable.picture_10,
//                R.drawable.picture_11, R.drawable.picture_12,
//                R.drawable.picture_13, R.drawable.picture_14,
//                R.drawable.picture_15, R.drawable.picture_16,
//                R.drawable.picture_17, R.drawable.picture_18,
//                R.drawable.picture_19, R.drawable.picture_20,
//                R.drawable.picture_21, R.drawable.picture_22};

        ImageAdapter adapter = new ImageAdapter(mContext, images);

        // 计算图片的宽高
        int[] dimension = BitmapScaleDownUtil
                .getScreenDimension(getWindowManager().getDefaultDisplay());
        int imageWidth = dimension[0] / SCALE_FACTOR;
        int imageHeight = dimension[1] / SCALE_FACTOR;
        // 初始化图片
        adapter.createImages(imageWidth, imageHeight);

        // 设置Adapter，显示位置位于控件中间，这样使得左右均可"无限"滑动
        mGalleryFlow = (GalleryFlow) findViewById(R.id.gallery_flow);
        mGalleryFlow.setSpacing(GALLERY_SPACING);
        mGalleryFlow.setAdapter(adapter);
        mGalleryFlow.setSelection(Integer.MAX_VALUE / 2);

        mGalleryFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedPictureId = ((ImageAdapter) parent.getAdapter())
                        .getItemRealId(position);
                Intent intent = new Intent();
                // interface changed.
                intent.setClass(ReadFromLocal.this, ResultPage.class);
                intent.putExtra("ClassName", "ReadFromLocal");
                intent.putExtra("selectedPictureId", selectedPictureId);
                intent.putExtra("MethodName", "selectPicture");
                startActivity(intent);
            }

        });

    }

    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
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
            intent.setClass(ReadFromLocal.this, ResultPage.class);
            intent.putExtra(KEY_PHOTO_PATH, picPath);
            intent.putExtra("MethodName", "takePhoto");
            intent.putExtra("ClassName", "SelectPicActivity");
            intent.putExtra("IsSketch", isSketch);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO){
//			imageView.setImageBitmap(null);
//			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
//			Log.i(TAG, "����ѡ���ͼƬ="+picPath);
//			txt.setText("�ļ�·��"+picPath);
//			Bitmap bm = BitmapFactory.decodeFile(picPath);
//			imageView.setImageBitmap(bm);
//	}
//		super.onActivityResult(requestCode, resultCode, data);
//    }
}
