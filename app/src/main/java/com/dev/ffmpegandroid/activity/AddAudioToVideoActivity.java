package com.dev.ffmpegandroid.activity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.dev.ffmpegandroid.R;
import com.dev.ffmpegandroid.util.Permission;
import com.dev.ffmpegandroid.util.Utils;
import com.github.pratikvekariya.ffmpeg.ExecuteBinaryResponseHandler;
import com.github.pratikvekariya.ffmpeg.FFmpeg;
import com.github.pratikvekariya.ffmpeg.LoadBinaryResponseHandler;
import com.github.pratikvekariya.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.pratikvekariya.ffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAudioToVideoActivity extends AppCompatActivity {

    String audioPath,silentVideoPath,final_video_with_audio;
    private FFmpeg ffmpeg;
    boolean isAudioAddedSuccess;

    @BindView(R.id.silentVideoView)
    VideoView silentVideoView;
    @BindView(R.id.withAudioVideoView)
    VideoView withAudioVideoView;
    @BindView(R.id.relativeWithVideo)
    RelativeLayout relativeWithVideo;
    @BindView(R.id.ivSilentPlay)
    ImageView ivSilentPlay;
    @BindView(R.id.ivAudioPlay)
    ImageView ivAudioPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audio_to_video);
        ButterKnife.bind(this);


        audioPath = Utils.getDefaultStorage(AddAudioToVideoActivity.this, "Audio").toString() + "/" + "audio.mp3";
        silentVideoPath  = new File(Utils.getDefaultStorage(this, "Videos"), "silent_video.mp4").getAbsolutePath();
        final_video_with_audio = new File(Utils.getDefaultStorage(this, "Videos"), "final_video_with_audio.mp4").getAbsolutePath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Permission.isPermission(AddAudioToVideoActivity.this)) {
                Permission.requestPermission(AddAudioToVideoActivity.this, 101);
            } else {
                File file1 = new File(Utils.getDefaultStorage(this, "Videos").getAbsolutePath());
                file1.mkdir();
                Utils.copyAssets(this,"video","Videos");
            }
        } else {
            File file1 = new File(Utils.getDefaultStorage(this, "Videos").getAbsolutePath());
            file1.mkdir();
            Utils.copyAssets(this,"video","Videos");
        }

        //check for audio copied or not
        File audioFile=new File(audioPath);
        if (!audioFile.exists()){
            Utils.copyAssets(this,"audio","Audio");
        }

        File finalVideoFile=new File(final_video_with_audio);

        if (finalVideoFile.exists()){
            finalVideoFile.delete();
        }

        loadFFMpegBinary();
    }

    @OnClick({R.id.ivBack, R.id.ivSilentPlay, R.id.btnAddAudio, R.id.ivAudioPlay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivSilentPlay:
                playSilentVideo();
                break;
            case R.id.btnAddAudio:
                Utils.showProgress(AddAudioToVideoActivity.this);
                runAddAudioCommand();
                break;
            case R.id.ivAudioPlay:
                playFinalVideo();

                break;
        }
    }

    private void playFinalVideo() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(withAudioVideoView);
        withAudioVideoView.setMediaController(mediaController);
        withAudioVideoView.setVideoPath(final_video_with_audio);


        if (withAudioVideoView.isPlaying()){
            withAudioVideoView.stopPlayback();
        }

        if (silentVideoView.isPlaying()){
            silentVideoView.stopPlayback();
        }

        withAudioVideoView.start();

        ivAudioPlay.setVisibility(View.GONE);

        withAudioVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivAudioPlay.setVisibility(View.VISIBLE);
            }
        });
    }

    private void playSilentVideo() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(silentVideoView);
        silentVideoView.setMediaController(mediaController);
        silentVideoView.setVideoPath(silentVideoPath);


        if (withAudioVideoView.isPlaying()){
            withAudioVideoView.stopPlayback();
        }

        if (silentVideoView.isPlaying()){
            silentVideoView.stopPlayback();
        }

        silentVideoView.start();

        ivSilentPlay.setVisibility(View.GONE);

        silentVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivSilentPlay.setVisibility(View.VISIBLE);
            }
        });
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
    private void execFFmpegBinary(final String[] command, final int commandNum) {

        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Toast.makeText(AddAudioToVideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    isAudioAddedSuccess=false;
                    Utils.dismissProgress();

                }

                @Override
                public void onSuccess(String s) {
                    isAudioAddedSuccess=true;
                }

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    switch (commandNum) {
                        case 1:
                            Utils.dismissProgress();
                            if (isAudioAddedSuccess){
                                relativeWithVideo.setVisibility(View.VISIBLE);
                            }else{
                                relativeWithVideo.setVisibility(View.GONE);
                            }
                            break;
                    }
                }
            }, this);
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            Utils.dismissProgress();
        }
    }


    private void runAddAudioCommand() {
        String[] commadAddAudio = {
                "-i"
                , silentVideoPath
                , "-i"
                , audioPath
                , "-c"
                , "copy"
                , "-map"
                , "0:v:0"
                , "-map"
                , "1:a:0"
                , "-strict"
                , "-1"
                , final_video_with_audio
        };
        //command add audio in video
        execFFmpegBinary(commadAddAudio, 1);
    }
}
