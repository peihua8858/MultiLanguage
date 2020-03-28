package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

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
        FieldVisitor fv = cw.visitField(Opcodes.ACC_PRIVATE, "mOverrideConfiguration",
                "Landroid/content/res/Configuration;", null, null);
        fv.visitEnd();
        fv = cw.visitField(Opcodes.ACC_PRIVATE, "mResources",
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
        mv.visitMethodInsn(INVOKESPECIAL, "android/app/Activity", OVERRIDE_METHOD,
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
}
