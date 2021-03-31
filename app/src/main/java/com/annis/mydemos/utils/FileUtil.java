package com.annis.mydemos.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

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
}
