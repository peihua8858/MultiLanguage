package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/10 16:04
 */
public class OverrideMethodAttachBaseContext extends AbstractOverrideMethodVisitor {
    final static String OVERRIDE_METHOD = "attachBaseContext";

    @Override
    protected boolean equalsMethod(String methodName) {
        return OVERRIDE_METHOD.equals(methodName);
    }

    @Override
    public boolean methodVisitor(ClassWriter cw, String className, String superClassName, String fileName) {
        if (isOverrideMethod(className, superClassName)) {
            if (ConfigsMethod.isActivity(className, superClassName)) {
                return callOverrideActivityMethod(cw, className, fileName);
            } else if (ConfigsMethod.isIntentService(className, superClassName)) {
                return calOverrideIntentServiceMethod(cw, className, fileName);
            } else if (ConfigsMethod.isService(className, superClassName)) {
                return calOverrideServiceMethod(cw, className, fileName);
            } else if (ConfigsMethod.isApplication(className, superClassName)) {
                return calOverrideApplicationMethod(cw, className, fileName);
            }
        }
        return false;
    }

    boolean calOverrideApplicationMethod(ClassWriter cw, String className, String fileName) {
        MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, OVERRIDE_METHOD,
                "(Landroid/content/Context;)V",
                null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESTATIC, "com/fz/multilanguages/MultiLanguage",
                "attachContext", "(Landroid/content/Context;)Landroid/content/Context;",
                false);
        mv.visitMethodInsn(INVOKESPECIAL, "android/app/Application", OVERRIDE_METHOD,
                "(Landroid/content/Context;)V", false);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitInsn(RETURN);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("newBase", "Landroid/content/Context;",
                null, l0, l2, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        FieldVisitor fv = cw.visitField(ACC_PRIVATE, "mOverrideConfiguration",
                "Landroid/content/res/Configuration;", null, null);
        fv.visitEnd();
        fv = cw.visitField(ACC_PRIVATE, "mResources",
                "Landroid/content/res/Resources;", null, null);
        fv.visitEnd();
        return true;
    }


    boolean calOverrideIntentServiceMethod(ClassWriter cw, String className, String fileName) {
        MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, OVERRIDE_METHOD,
                "(Landroid/content/Context;)V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESTATIC, "com/fz/multilanguages/MultiLanguage",
                "attachContext", "(Landroid/content/Context;)Landroid/content/Context;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "android/app/IntentService", OVERRIDE_METHOD,
                "(Landroid/content/Context;)V", false);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitInsn(RETURN);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("newBase", "Landroid/content/Context;",
                null, l0, l2, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        return true;
    }


    boolean calOverrideServiceMethod(ClassWriter cw, String className, String fileName) {
        MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, OVERRIDE_METHOD,
                "(Landroid/content/Context;)V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESTATIC, "com/fz/multilanguages/MultiLanguage",
                "attachContext", "(Landroid/content/Context;)Landroid/content/Context;",
                false);
        mv.visitMethodInsn(INVOKESPECIAL, "android/app/Service", OVERRIDE_METHOD,
                "(Landroid/content/Context;)V", false);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitInsn(RETURN);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("base", "Landroid/content/Context;",
                null, l0, l2, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        return true;
    }


    boolean callOverrideActivityMethod(ClassWriter cw, String className, String fileName) {
        MethodVisitor methodVisitor = cw.visitMethod(ACC_PROTECTED, OVERRIDE_METHOD,
                "(Landroid/content/Context;)V",
                null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
        Label label3 = new Label();
        Label label4 = new Label();
        methodVisitor.visitTryCatchBlock(label3, label4, label2, "java/lang/Exception");
        Label label5 = new Label();
        methodVisitor.visitLabel(label5);
        methodVisitor.visitLineNumber(18, label5);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "com/fz/multilanguages/MultiLanguage", "attachContext", "(Landroid/content/Context;)Landroid/content/Context;", false);
        methodVisitor.visitVarInsn(ASTORE, 2);
        Label label6 = new Label();
        methodVisitor.visitLabel(label6);
        methodVisitor.visitLineNumber(19, label6);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "androidx/appcompat/app/AppCompatActivity", "attachBaseContext", "(Landroid/content/Context;)V", false);
        methodVisitor.visitLabel(label0);
        methodVisitor.visitLineNumber(21, label0);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/Context", "getResources", "()Landroid/content/res/Resources;", false);
        methodVisitor.visitVarInsn(ASTORE, 3);
        Label label7 = new Label();
        methodVisitor.visitLabel(label7);
        methodVisitor.visitLineNumber(22, label7);
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/res/Resources", "getConfiguration", "()Landroid/content/res/Configuration;", false);
        methodVisitor.visitVarInsn(ASTORE, 4);
        Label label8 = new Label();
        methodVisitor.visitLabel(label8);
        methodVisitor.visitLineNumber(23, label8);
        methodVisitor.visitFieldInsn(GETSTATIC, "android/os/Build$VERSION", "SDK_INT", "I");
        methodVisitor.visitIntInsn(BIPUSH, 24);
        methodVisitor.visitJumpInsn(IF_ICMPLT, label3);
        Label label9 = new Label();
        methodVisitor.visitLabel(label9);
        methodVisitor.visitLineNumber(24, label9);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitVarInsn(ALOAD, 4);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AppCompatActivity", "applyOverrideConfiguration", "(Landroid/content/res/Configuration;)V", false);
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLineNumber(25, label1);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitLabel(label3);
        methodVisitor.visitLineNumber(27, label3);
        methodVisitor.visitFrame(F_APPEND, 3, new Object[]{"android/content/Context", "android/content/res/Resources", "android/content/res/Configuration"}, 0, null);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitVarInsn(ALOAD, 4);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AppCompatActivity", "applyOverrideConfiguration", "(Landroid/content/res/Configuration;)V", false);
        Label label10 = new Label();
        methodVisitor.visitLabel(label10);
        methodVisitor.visitLineNumber(28, label10);
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitVarInsn(ALOAD, 4);
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/res/Resources", "getDisplayMetrics", "()Landroid/util/DisplayMetrics;", false);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/res/Resources", "updateConfiguration", "(Landroid/content/res/Configuration;Landroid/util/DisplayMetrics;)V", false);
        methodVisitor.visitLabel(label4);
        methodVisitor.visitLineNumber(30, label4);
        Label label11 = new Label();
        methodVisitor.visitJumpInsn(GOTO, label11);
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLineNumber(29, label2);
        methodVisitor.visitFrame(F_FULL, 3, new Object[]{"androidx/appcompat/app/AppCompatActivity", "android/content/Context", "android/content/Context"}, 1, new Object[]{"java/lang/Exception"});
        methodVisitor.visitVarInsn(ASTORE, 3);
        methodVisitor.visitLabel(label11);
        methodVisitor.visitLineNumber(31, label11);
        methodVisitor.visitFrame(F_SAME, 0, null, 0, null);
        methodVisitor.visitInsn(RETURN);
        Label label12 = new Label();
        methodVisitor.visitLabel(label12);
        methodVisitor.visitLocalVariable("resources", "Landroid/content/res/Resources;", null, label7, label4, 3);
        methodVisitor.visitLocalVariable("configuration", "Landroid/content/res/Configuration;", null, label8, label4, 4);
        methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label5, label12, 0);
        methodVisitor.visitLocalVariable("newBase", "Landroid/content/Context;", null, label5, label12, 1);
        methodVisitor.visitLocalVariable("context", "Landroid/content/Context;", null, label6, label12, 2);
        methodVisitor.visitMaxs(3, 5);
        methodVisitor.visitEnd();
        return true;
    }
}
