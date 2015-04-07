package com.facematch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;

public class BitmapScaleDownUtil {
    public static int[] getScreenDimension(Display display) {
        int[] dimension = new int[2];
        dimension[0] = display.getWidth();
        dimension[1] = display.getHeight();

        return dimension;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // step1，将inJustDecodeBounds置为true，以解析Bitmap真实尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // step2，计算Bitmap取样比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // step3，将inJustDecodeBounds置为false，以取样比列解析Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 默认取样比例为1:1
        int inSampleSize = 1;

        // Bitmap原始尺寸
        final int width = options.outWidth;
        final int height = options.outHeight;

        // 取最大取样比例
        if (height > reqHeight || width > reqWidth) {
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            final int heightRatio = Math.round((float) height / (float) reqHeight);

            // 取样比例为X:1，其中X>=1
            inSampleSize = Math.max(widthRatio, heightRatio);
        }

        return inSampleSize;
    }
}
