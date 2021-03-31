package com.annis.mydemos.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.util.List;

/**
 * @author lee
 * @title: FileUtil
 * @description: TODO
 * @date 2021/3/31  14:30
 */
public class FileUtil {
    public static String authority;

    public static void setAuthority(String authority) {
        FileUtil.authority = authority;
    }

    public static Uri getUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return FileProvider.getUriForFile(context, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    //复杂版处理  (适配多种API)   最后直接调用这个方法就可以了
    public static String getPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        if (sdkVersion < 19) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
    }

    //简易处理板  （实际本没有发现什么问题，可以直接使用）
    public static String getRealPathFromUri(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},//
                null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            // 使用':'分割
            String id = wholeID.split(":")[1];

            String[] projection = {MediaStore.Images.Media.DATA};
            String selection = MediaStore.Images.Media._ID + "=?";
            String[] selectionArgs = {id};

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                    projection, selection, selectionArgs, null);
            int columnIndex = cursor.getColumnIndex(projection[0]);
            if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
            cursor.close();
        } else if ("content".equals(uri.getScheme())) {
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(projection[0]);
                filePath = cursor.getString(column_index);          //取出文件路径
                cursor.close();
            }
        } else if ("file".equals(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        //这个有两个包不知道是哪个。。。。不过这个复杂版一般用不到
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }
}
