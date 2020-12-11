package com.fz.multilanguages.plugin;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * 添加TryCatchMethod
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/11 11:57
 */
public class AddDynamicTryCatchMethodVisitor extends AdviceAdapter {
    private MethodVisitor mv;
    String methodName;
    String className;
    Label l1;
    Label l2;
    String descriptor;
    final Type returnType;

    public AddDynamicTryCatchMethodVisitor(MethodVisitor mv, String className, final int access,
                                           final String methodName,
                                           final String descriptor) {
        super(Opcodes.ASM7, mv, access, methodName, descriptor);
        this.mv = mv;
        this.methodName = methodName;
        this.className = className;
        this.descriptor = descriptor;
        returnType = Type.getReturnType(descriptor);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        if (returnType == Type.VOID_TYPE) {
            Label l0 = new Label();
            l1 = new Label();
            l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
            mv.visitLabel(l0);
        }
    }

    @Override
    protected void onMethodExit(int i) {
        super.onMethodExit(i);
        if (returnType == Type.VOID_TYPE) {
            mv.visitLabel(l1);
            Label l3 = new Label();
            mv.visitJumpInsn(GOTO, l3);
            mv.visitLabel(l2);
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
            Label label6 = new Label();
            mv.visitLocalVariable("e", "Ljava/lang/Throwable;", null, l1, l2, 1);
            mv.visitLocalVariable("this", "L" + className + ";", null, l3, label6, 0);
            mv.visitLabel(l3);
        }
    }
}
