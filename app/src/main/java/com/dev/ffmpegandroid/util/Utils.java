package com.dev.ffmpegandroid.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.dev.ffmpegandroid.R;
import com.dev.ffmpegandroid.activity.CutAudioActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static ProgressDialog pDialog;

    public static void showProgress(Context mContext) {
        pDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        pDialog.setMessage(mContext.getResources().getString(R.string.progres_msg));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void dismissProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public static File getDefaultStorage(Context context, String folder_name) {
        String path = Environment.getExternalStorageDirectory().toString() + "/";
        File savepicdir1 = new File(path + context.getString(R.string.folder_name));

        if (!savepicdir1.exists()){
            savepicdir1.mkdir();
        }

        File savepicdir = new File(path + context.getString(R.string.folder_name) + "/" + folder_name);

        if (!savepicdir.exists()) {
            savepicdir.mkdir();
        }

        return savepicdir;
    }


    public static void copyAssets(Activity activity,String assetsFolder, String saveFolderName) {
        AssetManager assetManager = activity.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(assetsFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(assetsFolder+"/" + filename);   // if files resides inside the "Files" directory itself
                out = new FileOutputStream(Utils.getDefaultStorage(activity, saveFolderName).toString() + "/" + filename);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
