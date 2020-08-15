package com.fz.multilanguages.plugin;

import java.util.HashMap;
import java.util.List;

final class ConfigsMethod {
    public final static HashMap<String, OverrideMethodVisitor> OVERRIDE_METHOD_VISITOR = new HashMap<>();
    public final static HashMap<String, OverrideMethodVisitor> ADD_METHOD_VISITOR = new HashMap<>();

    static {
        OVERRIDE_METHOD_VISITOR.put(OverrideMethodAttachBaseContext.OVERRIDE_METHOD, new OverrideMethodAttachBaseContext());
//        OVERRIDE_METHOD_VISITOR.put(OverrideMethodApplyOverrideConfiguration.OVERRIDE_METHOD, new OverrideMethodApplyOverrideConfiguration());
//        OVERRIDE_METHOD_VISITOR.put(OverrideMethodGetResources.OVERRIDE_METHOD, new OverrideMethodGetResources());
//        ADD_METHOD_VISITOR.put(AddChangeLanguageMethodVisitor.ADD_METHOD, new AddChangeLanguageMethodVisitor());
//        ADD_METHOD_VISITOR.put(AddGetResourcesInternalMethodVisitor.ADD_METHOD, new AddGetResourcesInternalMethodVisitor());
    }

    public static boolean hasMethod(String methodName) {
        return OVERRIDE_METHOD_VISITOR.containsKey(methodName);
    }

    /**
     * 是否强制覆盖重写attach方法
     *
     * @param overwriteClass
     * @param className
     * @return
     */
    public static boolean isOverwriteAttachMethod(List<String> overwriteClass, String className) {
        for (String clazz : overwriteClass) {
            String clazzName = clazz.replaceAll("\\.", "\\/");
            if (clazzName.equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否需要添加
     *
     * @return
     */
    public static boolean needAddOrOverride(String className, String superClassName) {
        return isActivity(className, superClassName)
                || isService(className, superClassName)
                || isIntentService(className, superClassName)
                || isApplication(className, superClassName);
    }

    /**
     * 如果是Application
     *
     * @author dingpeihua
     * @date 2020/1/9 14:47
     * @version 1.0
     */
    public static boolean isApplication(String className, String superClassName) {
        if (className == null || superClassName == null) {
            return false;
        }
        return ("androidx/multidex/MultiDexApplication".equals(superClassName)
                || "android/app/Application".equals(superClassName))
                && !isAndroidxPackageName(className) && !isAndroidSupportPackageName(className);
    }

    /**
     * 是否是Activity类
     *
     * @return
     */
    public static boolean isActivity(String className, String superClassName) {
        if (className == null || superClassName == null) {
            return false;
        }
        return ("android/support/v4/app/FragmentActivity".equals(superClassName)
                || "android/support/v7/app/AppCompatActivity".equals(superClassName)
                || "android/app/Activity".equals(superClassName)
                || isAndroidxActivity(superClassName))
                && !isAndroidxPackageName(className) && !isAndroidSupportPackageName(className);
    }

    /**
     * 是否是继承androidx.AppCompatActivity activity
     *
     * @return
     */
    public static boolean isAndroidxActivity(String superClassName) {
        if (superClassName == null) {
            return false;
        }
        return "androidx/appcompat/app/AppCompatActivity".equals(superClassName)
                || "androidx/fragment/app/FragmentActivity".equals(superClassName);
    }

    /**
     * 是否是继承androidx.AppCompatActivity activity
     *
     * @return
     */
    public static boolean isAndroidSupportPackageName(String className) {
        return className.startsWith("android/support");
    }

    /**
     * 是否是androidx包名下类
     *
     * @return
     */
    public static boolean isAndroidxPackageName(String className) {
        return className.contains("androidx/core/app")
                || className.contains("androidx/fragment/app")
                || className.startsWith("androidx/");
    }

    public static boolean isService(String className, String superClassName) {
        if (className == null || superClassName == null) {
            return false;
        }
        return "android/app/Service".equals(superClassName)
                && !isAndroidxPackageName(className);
    }

    public static boolean isIntentService(String className, String superClassName) {
        if (className == null || superClassName == null) {
            return false;
        }
        return "android/app/IntentService".equals(superClassName)
                && !isAndroidxPackageName(className);
    }
}
