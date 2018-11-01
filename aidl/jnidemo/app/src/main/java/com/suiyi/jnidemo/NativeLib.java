package com.suiyi.jnidemo;

public class NativeLib {

    static {
        System.loadLibrary("native-demo-lib");
    }

    public native static void printHello();

}
