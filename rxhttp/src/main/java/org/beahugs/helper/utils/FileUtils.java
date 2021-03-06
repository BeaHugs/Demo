package org.beahugs.helper.utils;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Locale;

/**
 * @Author: wangyibo
 * @Version: 1.0
 */
public class FileUtils {

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return "";
        }
        String fileName = file.getName();
        if (fileName.endsWith(".")) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index < 0) {
            return "";
        }
        return fileName.substring(index + 1).toLowerCase(Locale.US);
    }

    public static String getMimeType(File file){
        String suffix = getSuffix(file);
        String mimeType = null;
        if (!TextUtils.isEmpty(suffix)) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        }
        if (TextUtils.isEmpty(mimeType)) {
            mimeType = "file/*";
        }
        return mimeType;
    }
}
