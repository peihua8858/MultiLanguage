package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * 添加getResourcesInternal
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/11 11:57
 */
public class AddGetResourcesInternalMethodVisitor implements OverrideMethodVisitor {
    static final String ADD_METHOD = "getResourcesInternal";

    @Override
    public boolean methodVisitor(ClassWriter cw, String className, String superClassName, String fileName) {
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PRIVATE, "getResourcesInternal",
                "()Landroid/content/res/Resources;", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(48, l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, className, "mResources", "Landroid/content/res/Resources;");
        Label l1 = new Label();
        mv.visitJumpInsn(Opcodes.IFNONNULL, l1);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(49, l2);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, className, "mOverrideConfiguration", "Landroid/content/res/Configuration;");
        Label l3 = new Label();
        mv.visitJumpInsn(Opcodes.IFNONNULL, l3);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLineNumber(50, l4);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/app/Application", "getResources",
                "()Landroid/content/res/Resources;", false);
        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "mResources", "Landroid/content/res/Resources;");
        mv.visitJumpInsn(Opcodes.GOTO, l1);
        mv.visitLabel(l3);
        mv.visitLineNumber(52, l3);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, className, "mOverrideConfiguration", "Landroid/content/res/Configuration;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, "createConfigurationContext",
                "(Landroid/content/res/Configuration;)Landroid/content/Context;", false);
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLineNumber(53, l5);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/content/Context", "getResources",
                "()Landroid/content/res/Resources;", false);
        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "mResources", "Landroid/content/res/Resources;");
        mv.visitLabel(l1);
        mv.visitLineNumber(56, l1);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, className, "mResources", "Landroid/content/res/Resources;");
        mv.visitInsn(Opcodes.ARETURN);
        Label l6 = new Label();
        mv.visitLabel(l6);
        mv.visitLocalVariable("resContext", "Landroid/content/Context;", null, l5, l1, 1);
        mv.visitLocalVariable("this", "L" + className + ";", null, l0, l6, 0);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
        return true;
    }

    @Override
    public MethodVisitor visitMethod(ClassWriter classWriter, String className, String superClassName,
                                     List<String> overwriteClass,
                                     int access, String name, String descriptor, String signature, String[] exceptions) {
        return classWriter.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public String getMethod() {
        return ADD_METHOD;
    }

    @Override
    public boolean isOverrideMethod(String className, String superClassName) {
        return ConfigsMethod.isApplication(className, superClassName);
    }
}
