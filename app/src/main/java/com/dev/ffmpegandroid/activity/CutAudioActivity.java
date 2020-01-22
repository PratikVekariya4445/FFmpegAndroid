package com.dev.ffmpegandroid.activity;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dev.ffmpegandroid.R;
import com.dev.ffmpegandroid.util.Permission;
import com.dev.ffmpegandroid.util.Utils;
import com.github.pratikvekariya.ffmpeg.ExecuteBinaryResponseHandler;
import com.github.pratikvekariya.ffmpeg.FFmpeg;
import com.github.pratikvekariya.ffmpeg.LoadBinaryResponseHandler;
import com.github.pratikvekariya.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.pratikvekariya.ffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CutAudioActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private double startTime, endTime;
    private String mFileName;
    private FFmpeg ffmpeg;
    String outputCuttedAudio;
    boolean isCutSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut_audio);
        ButterKnife.bind(this);
        startTime = 0;
        endTime = 15;

        mFileName = Utils.getDefaultStorage(CutAudioActivity.this, "Audio").toString() + "/" + "audio.mp3";
        outputCuttedAudio = new File(Utils.getDefaultStorage(this, "Audio"), "outputCuttedAudio.mp3").getAbsolutePath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Permission.isPermission(CutAudioActivity.this)) {
                Permission.requestPermission(CutAudioActivity.this, 101);
            } else {
                File file1 = new File(Utils.getDefaultStorage(this, "Audio").getAbsolutePath());
                file1.mkdir();
                copyAssets();
            }
        } else {
            File file1 = new File(Utils.getDefaultStorage(this, "Audio").getAbsolutePath());
            file1.mkdir();
            copyAssets();
        }
        File file1 = new File(outputCuttedAudio);
        if (file1.exists()) {
            file1.delete();
        }

        loadFFMpegBinary();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            copyAssets();

        }
    }

    @OnClick({R.id.ivBack, R.id.btnCut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnCut:
                cutAudio();
                break;
        }
    }

    public void cutAudio() {

        String[] complexCommand = {
                "-i"
                , mFileName
                , "-ss"
                , String.valueOf(startTime)
                , "-to"
                , String.valueOf(endTime)
                , "-c"
                , "copy"
                , "-preset"
                , "ultrafast"
                , outputCuttedAudio
        };

        execFFmpegBinary(complexCommand);
    }


    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    isCutSuccess = false;
                    Toast.makeText(CutAudioActivity.this, "enable to select audio, Please try again!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                }

                @Override
                public void onSuccess(String s) {
                    isCutSuccess = true;
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CutAudioActivity.this, "Success", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                    int start = message.indexOf("time=");
                    int end = message.indexOf(" bitrate");
                    if (start != -1 && end != -1) {
                        String duration = message.substring(start + 5, end);
                        if (!duration.equals("")) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    progressBar.setVisibility(View.GONE);

                    if (isCutSuccess) {
                        //play cut audio
                        Toast.makeText(CutAudioActivity.this, "Finished", Toast.LENGTH_SHORT).show();
                    }
                }
            }, this);
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("audio");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("audio/" + filename);   // if files resides inside the "Files" directory itself
                out = new FileOutputStream(Utils.getDefaultStorage(CutAudioActivity.this, "Audio").toString() + "/" + filename);
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

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
