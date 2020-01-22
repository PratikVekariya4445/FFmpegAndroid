package com.dev.ffmpegandroid.util;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;



public class Permission {
    public static String[] STORAGE_PERMISSION = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public static String[] RECORD_PERMISSION = new String[]{
            android.Manifest.permission.RECORD_AUDIO,
    };

    public static void requestPermission(Activity act,int requestCode) {
        List<String> listPermissionsNeeded;
        int result;
        listPermissionsNeeded = new ArrayList<>();
        for (String p : STORAGE_PERMISSION) {
            result = ContextCompat.checkSelfPermission(act, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(act, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
        }
    }

    public static void requestRecordPermission(Activity act,int requestCode) {
        List<String> listPermissionsNeeded;
        int result;
        listPermissionsNeeded = new ArrayList<>();
        for (String p : RECORD_PERMISSION) {
            result = ContextCompat.checkSelfPermission(act, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(act, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
        }

    }

    public static boolean isPermission(Activity act) {
        List<String> listPermissionsNeeded;
        int result;
        listPermissionsNeeded = new ArrayList<>();
        for (String p : STORAGE_PERMISSION) {
            result = ContextCompat.checkSelfPermission(act, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {

            return false;
        } else {
            return true;
        }

    }

    public static boolean isRecordPermission(Activity act) {
        List<String> listPermissionsNeeded;
        int result;
        listPermissionsNeeded = new ArrayList<>();
        for (String p : RECORD_PERMISSION) {
            result = ContextCompat.checkSelfPermission(act, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {

            return false;
        } else {
            return true;
        }

    }
}
