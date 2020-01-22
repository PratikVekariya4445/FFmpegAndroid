# FFmpegAndroid

FFmpeg is a multimedia framework able to decode, encode, transcode, mux, demux, stream, filter and play almost any type of content. It is used for professional image and video processing, including for various TV companies.

## Supported Architecture
- armeabi-v7a
- arm64-v8a
- x86
- x86_64

## How to use?
- First step is to load ffmpeg

```sh

        try {
            FFmpeg  ffmpeg = FFmpeg.getInstance(this);
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


```

- Second step is run your command

```sh
    
    private void execFFmpegBinary(final String[] command, final int commandNum) {

        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                   Toast.makeText(this, "Succrss", Toast.LENGTH_SHORT).show();
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
                }
            }, this);
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


```
