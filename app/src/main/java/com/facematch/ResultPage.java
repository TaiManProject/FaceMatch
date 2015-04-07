package com.facematch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ResultPage extends Activity implements OnClickListener {

    public static String picPath = null;
    private final String TAG = "ResultPage";

    // private Context mContext;
    //
    // // 图片缩放倍率（相对屏幕尺寸的缩小倍率）
    // public static final int SCALE_FACTOR = 12;
    //
    // // 图片间距（控制各图片之间的距离）
    // private final int GALLERY_SPACING = -5;
    //
    // // 控件
    // private GalleryFlow mGalleryFlow;
    private int selectedPictureId;

    private ImageView imageView;

    private boolean isSketch;
    private ImageView pic1;
    private ImageView pic2;
    private ImageView pic3;
    private String info;
    private String info1;
    private String info2;
    private String info3;

    private ImageView synPhoto;

    private Button selectAnotherButton;

    private int isSketchInt;

    private Result result;
//    ArrayList<Person> persons;
//    ArrayList<ImageView> resultImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // mContext = getApplicationContext();
        setContentView(R.layout.result_page);

        // initGallery();

//        resultImages = new ArrayList<>();
//        persons = new ArrayList<>();

        imageView = (ImageView) findViewById(R.id.selectedPicture);
        imageView.setOnClickListener(this);
        selectAnotherButton = (Button) findViewById(R.id.selectAnother);

        synPhoto = (ImageView) findViewById(R.id.synPicture);
        synPhoto.setOnClickListener(this);

        pic1 = (ImageView) findViewById(R.id.pic1);
        pic1.setOnClickListener(this);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic2.setOnClickListener(this);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic3.setOnClickListener(this);
//        resultImages.add(pic1);
//        resultImages.add(pic2);
//        resultImages.add(pic3);

        Intent dataIntent = getIntent();
        String methodName = dataIntent.getStringExtra("MethodName");
        isSketch = dataIntent.getBooleanExtra("IsSketch", false);
        Bitmap bm = null;
        if (methodName.equals("takePhoto")) {
            imageView.setImageBitmap(null);
            picPath = dataIntent
                    .getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            Log.i(TAG, "4" + picPath);
            bm = BitmapFactory.decodeFile(picPath);
            imageView.setImageBitmap(bm);
        } else {
            selectedPictureId = dataIntent.getExtras().getInt(
                    "selectedPictureId");
            Resources res = getResources();
            bm = BitmapFactory.decodeResource(res, selectedPictureId);
            imageView.setImageResource(selectedPictureId);
        }

        /* Can add the boolean isSketch here*/
//        SendImage sendImage = new SendImage(,,,isSketch);

        Log.i(TAG, "isSketch" + isSketch);
        if (isSketch == true) isSketchInt = 1;
        else isSketchInt = 0;
//        SendImage sendImage = new SendImage("202.189.121.84", 54321, synPhoto, isSketchInt);
//        sendImage.execute(bm);
//        Connection connection = new Connection("147.8.22.105", 54321, synPhoto, isSketchInt);
        Connection connection = new Connection("192.168.2.45", 54321, synPhoto, isSketchInt);
        connection.execute(bm);


        selectAnotherButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(ResultPage.this, ReadFromLocal.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
//            case R.id.selectedPicture:
//                showDialog(ResultPage.this);
//                break;
//            case R.id.synPicture:
//                break;
            case R.id.pic1:
                info = info1;
                showDialog(ResultPage.this);
//                Log.i(TAG,"info1 is " + info1);
                break;
            case R.id.pic2:
                info = info2;
                showDialog(ResultPage.this);
//                Log.i(TAG,"info2 is " + info2);
                break;
            case R.id.pic3:
                info = info3;
                showDialog(ResultPage.this);
//                Log.i(TAG,"info3 is " + info3);
                break;
            default:
                finish();
                break;
        }

    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Information");
        builder.setMessage(info);
        builder.show();
    }

    class Connection extends SendImage {

        public Connection(String ip, int port, ImageView imageView, int isSketch) {
            super(ip, port, imageView, isSketch);
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            for (int i = 0; i < result.getPersons().size(); i++) {
                if (i == 0) {
                    pic1.setImageBitmap(result.getPersons().get(0).getPhoto());
                    info1 = result.getPersons().get(0).getInfo();
                } else if (i == 1) {
                    pic2.setImageBitmap(result.getPersons().get(1).getPhoto());
                    info2 = result.getPersons().get(1).getInfo();
                } else if (i == 2) {
                    pic3.setImageBitmap(result.getPersons().get(2).getPhoto());
                    info3 = result.getPersons().get(2).getInfo();
                }
            }
        }
    }

//
//    getMessage(){
//        get the message from socket;
//        char1[] = inform1;
//        char2[] = inform2;
//        char3[] = inform3;
//    }
//
//    then, builder.setMessage(char1\n + char2\n + char3);
}
