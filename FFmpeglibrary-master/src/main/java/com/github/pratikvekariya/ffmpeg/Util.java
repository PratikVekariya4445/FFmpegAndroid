package com.github.pratikvekariya.ffmpeg;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

class Util {

    static boolean isDebug(Context context) {
        return (0 != (context.getApplicationContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
    }

    static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    static String convertInputStreamToString(InputStream inputStream) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            StringBuilder sb = new StringBuilder();
            //old code
            /*int read = r.read(buffer, 0, buffer.length);
            while (read > 0) {
                output.append(buffer, 0, r);
            }*/

            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = r.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            r.close();
            /*while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            r.close();*/
            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void destroyProcess(Process process) {
        if (process != null)
            process.destroy();
    }

    static boolean killAsync(AsyncTask asyncTask) {
        return asyncTask != null && !asyncTask.isCancelled() && asyncTask.cancel(true);
    }

    static boolean isProcessCompleted(Process process) {
        try {
            if (process == null) return true;
            process.exitValue();
            return true;
        } catch (IllegalThreadStateException e) {
            // do nothing
        }
        return false;
    }

    public static void installBinaryFromRaw(Context context, InputStream rawStream, File file) {
        //final InputStream rawStream = context.getResources().openRawResource(resId);
        final OutputStream binStream = getFileOutputStream(file);

        if (rawStream != null && binStream != null) {
            pipeStreams(rawStream, binStream);

            try {
                rawStream.close();
                binStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            doChmod(file, 777);
        }
    }
    public static OutputStream getFileOutputStream(File file) {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void pipeStreams(InputStream is, OutputStream os) {
        byte[] buffer = new byte[4096];
        int count;
        try {
            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
        } catch (IOException e) {
            android.util.Log.e("===", "Error writing stream.", e);
        }
    }
    public static void doChmod(File file, int chmodValue) {
        final StringBuilder sb = new StringBuilder();
        sb.append("chmod");
        sb.append(' ');
        sb.append(chmodValue);
        sb.append(' ');
        sb.append(file.getAbsolutePath());

        try {
            Runtime.getRuntime().exec(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
