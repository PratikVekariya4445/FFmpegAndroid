package com.dev.ffmpegandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dev.ffmpegandroid.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btnCutAudio, R.id.btnAddGif, R.id.btnAddAudioToVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCutAudio:
                startActivity(new Intent(MainActivity.this,CutAudioActivity.class));
                break;
            case R.id.btnAddAudioToVideo:
                startActivity(new Intent(MainActivity.this,AddAudioToVideoActivity.class));
                break;
            case R.id.btnAddGif:
                break;
        }
    }
}
