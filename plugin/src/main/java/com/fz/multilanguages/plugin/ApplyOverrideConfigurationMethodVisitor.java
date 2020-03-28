package com.fz.multilanguages.plugin;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

/**
 * 重写Configuration
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/8 10:38
 */
public class ApplyOverrideConfigurationMethodVisitor extends MethodVisitor {
    private MethodVisitor mv;
    private String className;

    public ApplyOverrideConfigurationMethodVisitor(MethodVisitor mv, String className) {
        super(Opcodes.ASM7, mv);
        this.mv = mv;
        this.className = className;
    }

    @Override
    public void visitCode() {
        addSetTo(mv, className);
        super.visitCode();
    }

    static void addSetTo(MethodVisitor mv, String className) {
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 1);
        Label l1 = new Label();
        mv.visitJumpInsn(IFNULL, l1);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, className,
                "getBaseContext", "()Landroid/content/Context;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/content/Context", "getResources",
                "()Landroid/content/res/Resources;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/content/res/Resources",
                "getConfiguration", "()Landroid/content/res/Configuration;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "android/content/res/Configuration",
                "setTo", "(Landroid/content/res/Configuration;)V", false);
        mv.visitLabel(l1);
    }
}
