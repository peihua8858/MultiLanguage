package com.fz.multilanguages.demo;

public class ExceptionUtils {
    public static void uploadCatchedException(Throwable e) {
        if (e != null) {
            e.printStackTrace();
        }
    }

    public static boolean isAttached(FlutterJNI flutterJNI) {
        return flutterJNI != null && flutterJNI.isAttached();
    }
}
