package com.b96software.schoolplannerapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static Bitmap getImage(byte[] image) {

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
        return Bitmap.createScaledBitmap(bitmap, 512, nh, true);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static byte[] compressImage(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] image = getBytes(inputStream);

        //Decodes the byte Array
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );

        //Scales and compresses the bitmap Image
        bitmap =  Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    public static byte[] getImageData(Context context, Uri imageUri) throws IOException
    {
        //Compress the imported image
        InputStream stream = context.getContentResolver().openInputStream(imageUri);
        byte[] image = compressImage(stream);

        return image;
    }
}
