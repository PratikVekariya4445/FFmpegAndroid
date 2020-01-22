package com.github.pratikvekariya.ffmpeg.listeners;

import com.github.pratikvekariya.ffmpeg.listeners.ResponseHandler;

public interface FFmpegLoadBinaryResponseHandler extends ResponseHandler {

    /**
     * on Fail
     */
    public void onFailure();

    /**
     * on Success
     */
    public void onSuccess();

}
