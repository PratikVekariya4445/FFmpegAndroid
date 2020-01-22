package com.github.pratikvekariya.ffmpeg;

import android.content.Context;

import java.io.IOException;

class ShellCommand {

    Context context;

    Process run(Context context,String[] commandString) {
        this.context = context;

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commandString/*,temp,file*/);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return process;
    }

    CommandResult runWaitFor(String[] s) {
        Process process = run(this.context,s);

        Integer exitValue = null;
        String output = null;
        try {
            if (process != null) {
                exitValue = process.waitFor();

                if (CommandResult.success(exitValue)) {
                    output = Util.convertInputStreamToString(process.getInputStream());
                } else {
                    output = Util.convertInputStreamToString(process.getErrorStream());
                }
            }
        } catch (InterruptedException e) {
            Log.e("Interrupt exception", e);
        } finally {
            Util.destroyProcess(process);
        }

        return new CommandResult(CommandResult.success(exitValue), output);
    }

}