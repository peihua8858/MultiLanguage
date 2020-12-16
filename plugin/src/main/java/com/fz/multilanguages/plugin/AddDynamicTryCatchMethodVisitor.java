//package com.fz.multilanguages.plugin;
//
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.Type;
//import org.objectweb.asm.commons.AdviceAdapter;
//
///**
// * 添加TryCatchMethod
// *
// * @author dingpeihua
// * @version 1.0
// * @date 2020/1/11 11:57
// */
//public class AddDynamicTryCatchMethodVisitor extends AdviceAdapter {
//    private MethodVisitor mv;
//    String methodName;
//    String className;
//    private Label from = new Label();
//    private Label to = new Label();
//    private Label target = new Label();
//    String descriptor;
//    final Type returnType;
//
//    public AddDynamicTryCatchMethodVisitor(MethodVisitor mv, String className, final int access,
//                                           final String methodName,
//                                           final String descriptor) {
//        super(Opcodes.ASM7, mv, access, methodName, descriptor);
//        this.mv = mv;
//        this.methodName = methodName;
//        this.className = className;
//        this.descriptor = descriptor;
//        returnType = Type.getReturnType(descriptor);
//    }
//
//    @Override
//    protected void onMethodEnter() {
//        super.onMethodEnter();
//        if (returnType == Type.VOID_TYPE) {
//            mv.visitTryCatchBlock(from, to, target, "java/lang/Throwable");
//            mv.visitLabel(from);
//        }
//    }
//
//    @Override
//    protected void onMethodExit(int i) {
//        if (returnType == Type.VOID_TYPE) {
//            mv.visitJumpInsn(GOTO, to);
//            mv.visitLabel(target);
//            mv.visitVarInsn(ASTORE, 1);
//            mv.visitVarInsn(ALOAD, 1);
//            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
//            Label label6 = new Label();
//            mv.visitLocalVariable("e", "Ljava/lang/Throwable;", null, to, target, 1);
//            mv.visitLocalVariable("this", "L" + className + ";", null, to, label6, 0);
//            mv.visitLabel(to);
//        }
//        super.onMethodExit(i);
//    }
//}
