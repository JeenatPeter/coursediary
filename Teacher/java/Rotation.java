package com.example.course_diary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Rotation {

    public static String resaveBitmap( String filename) { //help for fix landscape photos
        File outPutFile = new File(Environment.getExternalStorageDirectory(),filename );
        String outp=outPutFile.getAbsolutePath();
        OutputStream outStream = null;
        if (outPutFile.exists()) {
            outPutFile.delete();
            outPutFile = new File(Environment.getExternalStorageDirectory(), filename);
        }
        try {
            // make a new bitmap from your file
            Bitmap bitmap = BitmapFactory.decodeFile(filename);
            int rotate=0;
            try {
                ExifInterface exif = new ExifInterface(filename);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            Bitmap rotatedBitmap=null;
            if(rotate!=0) {
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap = Bitmap.createScaledBitmap(rotatedBitmap, (int) ((float) bitmap.getWidth() * 0.3f), (int) ((float) bitmap.getHeight() * 0.3f), false);
            }
            outPutFile.createNewFile();
            outStream = new FileOutputStream(outPutFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outp;
    }
}
