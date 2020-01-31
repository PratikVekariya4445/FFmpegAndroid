# FFmpegAndroid

FFmpeg is a multimedia framework able to decode, encode, transcode, mux, demux, stream, filter and play almost any type of content. It is used for professional image and video processing, including for various TV companies.

## Supported Architecture
- armeabi-v7a
- arm64-v8a
- x86
- x86_64

## Sample output

![ezgif com-optimize](https://user-images.githubusercontent.com/59924303/73561868-e151ef00-447f-11ea-90f7-2ddcbe89ca61.gif)


## Add dependency
- In your project’s build.gradle add the following line

```sh
        allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```

- Now in your app’s build.gradle add the dependency
```sh
       dependencies {
	        implementation 'com.github.PratikVekariya4445:FFmpegAndroid:1.0'
	}

```
## How to use?
- First step is to load ffmpeg and then execute your command

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
## Example : cut audio

```sh
public void cutAudio() {

        String[] command = {
                "-i"
                , inputAudioFilePath
                , "-ss"
                , String.valueOf(startTime)
                , "-to"
                , String.valueOf(endTime)
                , "-c"
                , "copy"
                , "-preset"
                , "ultrafast"
                , outputAudioPath
        };

        execFFmpegBinary(command);
    }
```

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
