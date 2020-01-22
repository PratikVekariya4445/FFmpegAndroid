package com.github.pratikvekariya.ffmpeg;

import android.os.Build;

class CpuArchHelper {
    public static final String X86_CPU = "x86";
    public static final String X86_64_CPU = "x86_64";
    public static final String ARM_64_CPU = "arm64-v8a";
    public static final String ARM_V7_CPU = "armeabi-v7a";

    static CpuArch getCpuArch() {
        // check if device is x86 or x86_64
        /*if (Build.CPU_ABI.equals(getx86CpuAbi())) {
            return CpuArch.x86;
        } else if(Build.CPU_ABI.equals(getx86_64CpuAbi())){
            return CpuArch.x86_64;
        }else {
            // check if device is armeabi
            if (Build.CPU_ABI.equals(getArmeabiv7CpuAbi())) {
                ArmArchHelper cpuNativeArchHelper = new ArmArchHelper();
                String archInfo = cpuNativeArchHelper.cpuArchFromJNI();
                // check if device is arm v7
                if (cpuNativeArchHelper.isARM_v7_CPU(archInfo)) {
                    // check if device is neon
                    return CpuArch.ARMv7;
                }
                // check if device is arm64 which is supported by ARMV7
            } else if (Build.CPU_ABI.equals(getArm64CpuAbi())) {
                return CpuArch.ARMv8;
            }
        }*/
        switch (Build.CPU_ABI) {
            case X86_CPU:
                return CpuArch.x86;
            case X86_64_CPU:
                return CpuArch.x86;
            case ARM_64_CPU:
                return CpuArch.x86;
            case ARM_V7_CPU:
                return CpuArch.ARMv7;
            default:
                return CpuArch.x86;
            //return CpuArch.NONE;
        }

    }

    static String getx86CpuAbi() {
        return "x86";
    }

    static String getx86_64CpuAbi() {
        return "x86_64";
    }

    static String getArm64CpuAbi() {
        return "arm64-v8a";
    }

    static String getArmeabiv7CpuAbi() {
        return "armeabi-v7a";
    }
}
