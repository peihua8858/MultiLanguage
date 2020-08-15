//package com.fz.multilanguages.plugin;
//
//import org.objectweb.asm.AnnotationVisitor;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//
//import java.util.List;
//
///**
// * 添加changeLanguage
// *
// * @author dingpeihua
// * @version 1.0
// * @date 2020/1/11 11:57
// */
//public class AddChangeLanguageMethodVisitor implements OverrideMethodVisitor {
//    static final String ADD_METHOD = "changeLanguage";
//
//    @Override
//    public boolean methodVisitor(ClassWriter cw, String className, String superClassName, String fileName) {
//        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, className, null, superClassName,
//                new String[]{"com/fz/multilanguages/ChangeLanguageListener"});
//        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "changeLanguage", "(Landroid/content/Context;)V", null, null);
//        AnnotationVisitor annotationVisitor0 = mv.visitAnnotation("Landroidx/annotation/Keep;", false);
//        annotationVisitor0.visitEnd();
//        mv.visitCode();
//        Label l0 = new Label();
//        mv.visitLabel(l0);
//        mv.visitLineNumber(72, l0);
//        mv.visitVarInsn(Opcodes.ALOAD, 0);
//        mv.visitTypeInsn(Opcodes.NEW, "android/content/res/Configuration");
//        mv.visitInsn(Opcodes.DUP);
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/content/Context",
//                "getResources", "()Landroid/content/res/Resources;", false);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/content/res/Resources",
//                "getConfiguration", "()Landroid/content/res/Configuration;", false);
//        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/content/res/Configuration",
//                "<init>", "(Landroid/content/res/Configuration;)V", false);
//        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "mOverrideConfiguration", "Landroid/content/res/Configuration;");
//        Label l1 = new Label();
//        mv.visitLabel(l1);
//        mv.visitLineNumber(73, l1);
//        mv.visitVarInsn(Opcodes.ALOAD, 0);
//        mv.visitVarInsn(Opcodes.ALOAD, 1);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/content/Context", "getResources",
//                "()Landroid/content/res/Resources;", false);
//        mv.visitFieldInsn(Opcodes.PUTFIELD, className, "mResources", "Landroid/content/res/Resources;");
//        Label l2 = new Label();
//        mv.visitLabel(l2);
//        mv.visitLineNumber(74, l2);
//        mv.visitInsn(Opcodes.RETURN);
//        Label l3 = new Label();
//        mv.visitLabel(l3);
//        mv.visitLocalVariable("this", "L" + className + ";", null, l0, l3, 0);
//        mv.visitLocalVariable("context", "Landroid/content/Context;", null, l0, l3, 1);
//        mv.visitMaxs(4, 2);
//        mv.visitEnd();
//        return true;
//    }
//
//    @Override
//    public MethodVisitor visitMethod(ClassWriter classWriter, String className, String superClassName, List<String> overwriteClass, int access, String name, String descriptor, String signature, String[] exceptions) {
//        return classWriter.visitMethod(access, name, descriptor, signature, exceptions);
//    }
//
//    @Override
//    public String getMethod() {
//        return null;
//    }
//
//    @Override
//    public boolean isOverrideMethod(String className, String superClassName) {
//        return ConfigsMethod.isApplication(className, superClassName);
//    }
//}
