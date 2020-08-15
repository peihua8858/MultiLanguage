package com.fz.multilanguages.demo;

public class ExceptionUtils {
    public static void uploadCatchedException(Throwable e) {
        if (e != null) {
            e.printStackTrace();
        }
    }
}
