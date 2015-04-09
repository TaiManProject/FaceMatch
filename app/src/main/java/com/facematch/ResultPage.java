package com.facematch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class ResultPage extends Activity implements OnClickListener {

    public static String picPath = null;
    private final String TAG = "ResultPage";
    private int selectedPictureId;

    private boolean isSketch;
    private int isSketchInt;

    private ImageView imageView;
    private ImageView synPhoto;
    private ImageView pic1;
    private ImageView pic2;
    private ImageView pic3;
    private ImageView testingImageView;

    private String info;
    private String info1;
    private String info2;
    private String info3;

    private Bitmap person1Photo;

    private Result localresult;
    private ArrayList<Person> persons;
    private Person localPerson;
    private View localView;
    private int intLocalButton;
//    private Bitmap drawableBitmap;

    private Button selectAnotherButton;
    // the buttons in popupwindow
    private Button btn_full;
    private Button btn_half;
    private Button btn_quarter;
    private Button btn_octant;
    private Button btn_landmarks;
    private Button btn;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        localPerson = new Person();

        imageView = (ImageView) findViewById(R.id.selectedPicture);
//        testingImageView = (ImageView) findViewById(R.id.testingImageView);
        selectAnotherButton = (Button) findViewById(R.id.selectAnother);

        synPhoto = (ImageView) findViewById(R.id.synPicture);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        pic1 = (ImageView) findViewById(R.id.pic1);
        pic1.setOnClickListener(this);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic2.setOnClickListener(this);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic3.setOnClickListener(this);

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

        Log.i(TAG, "isSketch" + isSketch);
        if (isSketch == true) isSketchInt = 1;
        else isSketchInt = 0;

//        Connection connection = new Connection("147.8.45.122", 54321, synPhoto, isSketchInt);
        Connection connection = new Connection("192.168.2.45", 54321, synPhoto, isSketchInt);
        connection.execute(bm);
        progressBar.setVisibility(View.VISIBLE);

        selectAnotherButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ResultPage.this, ReadFromLocal.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic1:
                info = info1;
//                showDialog(ResultPage.this);
                localPerson = persons.get(0);
                showPopupWindow(localPerson);
//                testingImageView.setImageBitmap(localPerson.getPhoto());
                break;
            case R.id.pic2:
                info = info2;
                showDialog(ResultPage.this);
                break;
            case R.id.pic3:
                info = info3;
                showDialog(ResultPage.this);
                break;
            case R.id.btn_full:
                intLocalButton = 1;
                drawSimilarities(intLocalButton, localPerson, localView);
                break;
            case R.id.btn_half:
                intLocalButton = 2;
                drawSimilarities(intLocalButton, localPerson, localView);
                break;
            case R.id.btn_quarter:
                intLocalButton = 4;
                drawSimilarities(intLocalButton, localPerson, localView);
                break;
            case R.id.btn_octant:
                intLocalButton = 8;
                drawSimilarities(intLocalButton, localPerson, localView);
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

    private void showPopupWindow(Person person) {
//        testingImageView.setImageBitmap(localPerson.getPhoto());
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.show_similarities, null);
        localView = view;

        // 初始化BUTTON仔们
        btn_full = (Button) view.findViewById(R.id.btn_full);
        btn_full.setOnClickListener(this);
        btn_half = (Button) view.findViewById(R.id.btn_half);
        btn_half.setOnClickListener(this);
        btn_quarter = (Button) view.findViewById(R.id.btn_quarter);
        btn_quarter.setOnClickListener(this);
        btn_octant = (Button) view.findViewById(R.id.btn_octant);
        btn_octant.setOnClickListener(this);
        btn_landmarks = (Button) view.findViewById(R.id.btn_landmarks);
        btn_landmarks.setOnClickListener(this);

        // 创建PopupWindow对象
        final PopupWindow pop = new PopupWindow(view, ActionMenuView.LayoutParams.WRAP_CONTENT, ActionMenuView.LayoutParams.WRAP_CONTENT, false);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        ImageView matchPerson = (ImageView) view.findViewById(R.id.matchPerson);
        Bitmap bitmap = person.getPhoto();
        matchPerson.setImageBitmap(bitmap);

        if (pop.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
            pop.dismiss();
        } else {
            // 显示窗口
            pop.showAtLocation(view, Gravity.NO_GRAVITY, 100, 100);
        }
    }

    private void drawSimilarities(int intButton, Person person, View view) {
        Bitmap drawableBitmap = person.getPhoto().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawableBitmap);
        //实例一个画笔
        Paint paint = new Paint();
        //设置画笔颜色为红色
        paint.setColor(Color.RED);
        //----利用填充画布，刷屏
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(50, 80, 150, 210, paint);
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.FILL);
        switch (intButton) {
            case 1:
                canvas.drawText(format(person.getSimilarities().get(0)), 50, 210, paint);
                break;
            case 2:
                canvas.drawLine(50, 145, 150, 145, paint);
                canvas.drawText(format(person.getSimilarities().get(1)), 50, 145, paint);
                canvas.drawText(format(person.getSimilarities().get(2)), 50, 210, paint);
                break;
            case 4:
                canvas.drawLine(50, 145, 150, 145, paint);
                canvas.drawLine(100, 80, 100, 210, paint);
                canvas.drawText(format(person.getSimilarities().get(3)), 50, 145, paint);
                canvas.drawText(format(person.getSimilarities().get(4)), 100, 145, paint);
                canvas.drawText(format(person.getSimilarities().get(5)), 50, 210, paint);
                canvas.drawText(format(person.getSimilarities().get(6)), 100, 210, paint);
                break;
            case 8:
                canvas.drawLine(50, 145, 150, 145, paint);
                canvas.drawLine(100, 80, 100, 210, paint);
                canvas.drawLine(50, 112, 150, 112, paint);
                canvas.drawLine(50, 177, 150, 177, paint);
                canvas.drawText(format(person.getSimilarities().get(7)), 50, 112, paint);
                canvas.drawText(format(person.getSimilarities().get(8)), 100, 112, paint);
                canvas.drawText(format(person.getSimilarities().get(9)), 50, 145, paint);
                canvas.drawText(format(person.getSimilarities().get(10)), 100, 145, paint);
                canvas.drawText(format(person.getSimilarities().get(11)), 50, 177, paint);
                canvas.drawText(format(person.getSimilarities().get(12)), 100, 177, paint);
                canvas.drawText(format(person.getSimilarities().get(13)), 50, 210, paint);
                canvas.drawText(format(person.getSimilarities().get(14)), 100, 210, paint);
                break;
            default:
                finish();
                break;
        }
//        int intSimilarity = new Double(person.getSimilarities().get(0)).intValue();
//        paint.setTextSize(25);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawText(intSimilarity + "", 50, 210, paint);
        ImageView matchPerson = (ImageView) view.findViewById(R.id.matchPerson);
        matchPerson.setImageBitmap(drawableBitmap);
    }

    private void displayMatchPersons(ArrayList<Person> persons) {
        for (int i = 0; i < persons.size(); i++) {
            if (i == 0) {
                pic1.setImageBitmap(persons.get(0).getPhoto());
                info1 = persons.get(0).getInfo();
                person1Photo = persons.get(0).getPhoto().copy(Bitmap.Config.ARGB_8888, true);
            } else if (i == 1) {
                pic2.setImageBitmap(persons.get(1).getPhoto());
                info2 = persons.get(1).getInfo();
            } else if (i == 2) {
                pic3.setImageBitmap(persons.get(2).getPhoto());
                info3 = persons.get(2).getInfo();
            }
        }
    }
    public static String format(double value) {
        return String.format("%.0f", value).toString();
    }

    class Connection extends SendImage {

        public Connection(String ip, int port, ImageView imageView, int isSketch) {
            super(ip, port, imageView, isSketch);
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            localresult = result;
            persons = result.getPersons();
            displayMatchPersons(persons);
            progressBar.setVisibility(View.GONE);
        }
    }

}