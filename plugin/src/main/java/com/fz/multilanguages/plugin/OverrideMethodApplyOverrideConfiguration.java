package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * 重写Configuration
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/8 10:38
 */
public class OverrideMethodApplyOverrideConfiguration extends AbstractOverrideMethodVisitor {
    final static String OVERRIDE_METHOD = "applyOverrideConfiguration";

    @Override
    protected boolean equalsMethod(String methodName) {
        return OVERRIDE_METHOD.equals(methodName);
    }

    @Override
    public boolean methodVisitor(ClassWriter cw, String className, String superClassName, String fileName) {
        if (isOverrideMethod(className, superClassName)) {
            if (ConfigsMethod.isActivity(className, superClassName)) {
                return callOverrideActivityMethod(cw, className, superClassName, fileName);
            }
        }
        return false;
    }

    @Override
    public MethodVisitor visitMethod(ClassWriter classWriter, String className, String superClassName, List<String> overwriteClass,
                                     int access, String name, String descriptor, String signature, String[] exceptions) {
       if (ConfigsMethod.needAddOrOverride(className, superClassName)) {
            if (ConfigsMethod.isAndroidxActivity(superClassName) && equalsMethod(name)) {
                isDeleteFormerMethod = true;
                return null;
            } else {
                isDeleteFormerMethod = false;
                return new ApplyOverrideConfigurationMethodVisitor(classWriter.visitMethod(access, name, descriptor, signature, exceptions), className);
            }
        }
        return classWriter.visitMethod(access, name, descriptor, signature, exceptions);
    }

    boolean callOverrideActivityMethod(ClassWriter cw, String className, String superClassName, String fileName) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "applyOverrideConfiguration",
                "(Landroid/content/res/Configuration;)V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 1);
        Label l1 = new Label();
        mv.visitJumpInsn(IFNULL, l1);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, className, "getBaseContext",
                "()Landroid/content/Context;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/content/Context", "getResources",
                "()Landroid/content/res/Resources;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/content/res/Resources", "getConfiguration",
                "()Landroid/content/res/Configuration;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/content/res/Configuration", "setTo",
                "(Landroid/content/res/Configuration;)V", false);
        mv.visitLabel(l1);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESPECIAL, superClassName,
                "applyOverrideConfiguration", "(Landroid/content/res/Configuration;)V", false);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitInsn(RETURN);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLocalVariable("this", "L" + className + ";", null, l0, l4, 0);
        mv.visitLocalVariable("overrideConfiguration", "Landroid/content/res/Configuration;",
                null, l0, l4, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        return false;
    }
}
