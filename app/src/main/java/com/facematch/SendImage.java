package com.facematch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.Point;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendImage extends AsyncTask<Bitmap, Integer, Result> {

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public int numOfPerson;
    ImageView imageView;
    int isSketch;
    private String ip;
    private int port;

    public SendImage(String ip, int port, ImageView imageView, int isSketch) {
        this.ip = ip;
        this.port = port;
        this.imageView = imageView;
        this.isSketch = isSketch;
        Log.e("debug", "com.facematch.SendImage.");
    }

    public static byte[] toByteArray(int iSource, int iArrayLen) {
        byte[] bLocalArr = new byte[iArrayLen];
        for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
            bLocalArr[i] = (byte) (iSource >> 8 * (3 - i) & 0xFF);
        }
        return bLocalArr;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    protected Result doInBackground(Bitmap... bitmaps) {
        int count = bitmaps.length;
        for (int i = 0; i < count && i < 1; ++i) {
            try {
                Result result = new Result();
                Socket socket = new Socket(ip, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                int length = bytes.length;
                Log.e("debug", String.valueOf(length));
                out.write(toByteArray(length, 4), 0, 4);
                Log.e("debug", String.valueOf(isSketch));
                out.write(toByteArray(isSketch, 4), 0, 4);
//                Log.e("debug", toByteArray(length,4).toString());
                out.write(bytes, 0, length);
                out.flush();
                DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                numOfPerson = dataInput.readInt();
                result.setFoundMethod(dataInput.readInt());
                Log.e("debug", "numOfPerson" + String.valueOf(numOfPerson));
                length = dataInput.readInt();
                Log.e("debug", "length" + String.valueOf(length));
                byte[] data = new byte[length];
                int dataReceived = 0;
                while (dataReceived < length) {
                    dataReceived += dataInput.read(data, dataReceived, length - dataReceived);
//                    Log.e("debug", "dataR" + String.valueOf(dataReceived));
                }
                result.setSynPhoto(BitmapFactory.decodeByteArray(data, 0, data.length));
                for (int j = 0; j < numOfPerson; j++) {
                    Person person = new Person();
                    length = dataInput.readInt();
                    Log.e("debug", "length:" + String.valueOf(length));
                    data = new byte[length];
                    dataReceived = 0;
                    while (dataReceived < length) {
                        dataReceived += dataInput.read(data, dataReceived, length - dataReceived);
                    }
                    String info = new String(data);
//                    Log.e("debug", bytesToHex(data));
                    person.setInfo(info);
                    Log.e("debug", info);

                    length = dataInput.readInt();
                    Log.e("debug", "length of similarities " + length);
                    for(int k = 0; k < length; k++) {
                        person.similarities.add(dataInput.readDouble());
                    }

                    length = dataInput.readInt() * 2;
                    Log.e("debug", "length of landmarks " + length);
                    for(int k = 0; k < length; k++) {
                        person.landmarks.add((float)dataInput.readInt());
                    }

                    length = dataInput.readInt();
                    Log.e("debug", "length." + String.valueOf(length));
                    data = new byte[length];
                    dataReceived = 0;
                    while (dataReceived < length) {
                        dataReceived += dataInput.read(data, dataReceived, length - dataReceived);
                        Log.e("debug", "dataReceived" + dataReceived + " of " + length);
                    }
//                    dataInput.readInt();
                    person.setPhoto(BitmapFactory.decodeByteArray(data, 0, data.length));
                    result.addPerson(person);
                }
                socket.close();
                return result;
            } catch (IOException e) {
                Log.e("debug", "IOException");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Result result) {
        imageView.setImageBitmap(result.getSynPhoto());
        Log.e("debug", "onPostExecute");
//        imageView.setImageBitmap(result.getPersons().get(2).getPhoto());
    }
}