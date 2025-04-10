package com.example.hcmuteforums.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        File file = new File(context.getCacheDir(), UUID.randomUUID().toString() + ".jpg");
        try (InputStream input = context.getContentResolver().openInputStream(uri);
             OutputStream output = new FileOutputStream(file)) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
