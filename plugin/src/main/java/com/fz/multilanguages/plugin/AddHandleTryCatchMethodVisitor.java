package com.fz.multilanguages.plugin;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.Map;

/**
 * 添加TryCatchMethod
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/11 11:57
 */
public class AddHandleTryCatchMethodVisitor extends AdviceAdapter {
    private MethodVisitor mv;
    private String methodName;
    private String className;
    private Label l1;
    private Label l2;
    private String exceptionHandleClass;
    private String exceptionHandleMethod;

    public AddHandleTryCatchMethodVisitor(MethodVisitor mv, String className, final int access,
                                          final String methodName,
                                          final String descriptor, String exceptionHandleClass,
                                          String exceptionHandleMethod) {
        super(Opcodes.ASM7, mv, access, methodName, descriptor);
        this.mv = mv;
        this.methodName = methodName;
        this.className = className;
        this.exceptionHandleClass = exceptionHandleClass;
        this.exceptionHandleMethod = exceptionHandleMethod;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        Label l0 = new Label();
        l1 = new Label();
        l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
        mv.visitLabel(l0);
    }

    @Override
    protected void onMethodExit(int i) {
        super.onMethodExit(i);
        mv.visitLabel(l1);
        Label l3 = new Label();
        mv.visitJumpInsn(GOTO, l3);
        mv.visitLabel(l2);
        mv.visitVarInsn(ASTORE, 1);
        if (exceptionHandleClass != null && exceptionHandleMethod != null) {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, exceptionHandleClass,
                    exceptionHandleMethod, "(Ljava/lang/Throwable;)V", false);
        }
        mv.visitLabel(l3);
    }
}
