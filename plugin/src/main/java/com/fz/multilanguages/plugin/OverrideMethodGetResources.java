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
 * 重写getResources方法
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/10 16:06
 */
public class OverrideMethodGetResources extends AbstractOverrideMethodVisitor {
    static final String OVERRIDE_METHOD = "getResources";

    @Override
    protected boolean equalsMethod(String methodName) {
        return OVERRIDE_METHOD.equals(methodName);
    }

    @Override
    public boolean methodVisitor(ClassWriter cw, String className, String superClassName, String fileName) {
        if (isOverrideMethod(className, superClassName)) {
            if (ConfigsMethod.isApplication(className, superClassName)) {
                return calOverrideApplicationMethod(cw, className, fileName);
            }
        }
        return false;
    }

    boolean calOverrideApplicationMethod(ClassWriter cw, String className, String fileName) {
        MethodVisitor methodVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC, "getResources",
                "()Landroid/content/res/Resources;", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitLineNumber(48, label0);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, className,
                "getResourcesInternal", "()Landroid/content/res/Resources;", false);
        methodVisitor.visitInsn(Opcodes.ARETURN);
        Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        String descriptor = "L" + className + ";";
        methodVisitor.visitLocalVariable("this", descriptor,
                null, label0, label1, 0);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
        return true;
    }
}
