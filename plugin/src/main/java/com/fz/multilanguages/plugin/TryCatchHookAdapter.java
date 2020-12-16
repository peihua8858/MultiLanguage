package com.fz.multilanguages.plugin;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 为指定方法添加try catch
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/12/16 17:10
 */
public class TryCatchHookAdapter extends AdviceAdapter {
    private Label from = new Label();
    private Label to = new Label();
    private Label target = new Label();
    private String exceptionHandleClass;
    private String exceptionHandleMethod;
    private String className;

    protected TryCatchHookAdapter(MethodVisitor methodVisitor, String className, int access, String name,
                                  String descriptor, String exceptionHandleClass, String exceptionHandleMethod) {
        super(Opcodes.ASM7, methodVisitor, access, name, descriptor);
        this.exceptionHandleClass = exceptionHandleClass;
        this.exceptionHandleMethod = exceptionHandleMethod;
        this.className = className;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        mv.visitTryCatchBlock(from, to, target, "java/lang/Throwable");
        mv.visitLabel(from);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        //标志：try块结束
        mv.visitLabel(to);
        //标志：catch块开始位置
        mv.visitLabel(target);
        mv.visitFrame(Opcodes.F_FULL, 0, null, 1, new Object[]{"java/lang/Throwable"});
        // 抛出异常
        if (exceptionHandleClass != null && exceptionHandleMethod != null) {
            mv.visitMethodInsn(INVOKESTATIC, exceptionHandleClass, exceptionHandleMethod, "(Ljava/lang/Throwable;)V",
                    false);
        } else {
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
        }
        mv.visitLocalVariable("e", "Ljava/lang/Throwable;", null, to, target, 1);
        Label label4 = new Label();
        mv.visitLabel(label4);
        Type returnType = getReturnType();
        if (returnType != Type.VOID_TYPE) {
            setVisitInsn(mv, returnType);
        }
        mv.visitInsn(returnType.getOpcode(Opcodes.IRETURN));
        super.visitMaxs(maxStack, maxLocals);
    }

    private void setVisitInsn(MethodVisitor mv, final Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
                mv.visitInsn(ICONST_0);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
//                mv.visitInsn(ARETURN);
                break;
            case Type.BYTE:
                mv.visitInsn(ICONST_0);
                mv.visitInsn(I2B);
//                mv.visitInsn(IRETURN);
                break;
            case Type.SHORT:
                mv.visitInsn(ICONST_0);
                mv.visitInsn(I2S);
//                mv.visitInsn(IRETURN);
                break;
            case Type.CHAR:
                mv.visitInsn(ICONST_0);
                mv.visitInsn(I2C);
//                mv.visitInsn(IRETURN);
                break;
            case Type.INT:
                mv.visitInsn(ICONST_0);
//                mv.visitInsn(IRETURN);
                break;
            case Type.FLOAT:
                mv.visitInsn(FCONST_0);
//                mv.visitInsn(FRETURN);
                break;
            case Type.LONG:
                mv.visitInsn(LCONST_0);
                mv.visitInsn(LRETURN);
//                mv.visitInsn(type.getOpcode(Opcodes.IRETURN));
                break;
            case Type.DOUBLE:
                mv.visitInsn(DCONST_0);
                mv.visitInsn(DRETURN);
//                mv.visitInsn(type.getOpcode(Opcodes.IRETURN));
                break;
            case Type.ARRAY:
                mv.visitInsn(ICONST_0);
                mv.visitTypeInsn(ANEWARRAY, getClassName(type.getInternalName()));
//                mv.visitInsn(ARETURN);
                break;
            case Type.OBJECT:
            default:
                String internalName = type.getInternalName();
                if (isBoolean(internalName)) {
                    mv.visitInsn(ICONST_0);
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                } else if (isString(internalName)) {
                    mv.visitLdcInsn("");
                } else if (isList(internalName)) {
                    mv.visitTypeInsn(NEW, "java/util/ArrayList");
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
                } else if (isMap(internalName)) {
                    mv.visitTypeInsn(NEW, "java/util/HashMap");
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
                } else if (isSet(internalName)) {
                    mv.visitTypeInsn(NEW, "java/util/HashSet");
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false);
                } else if (isQueue(internalName)) {
                    mv.visitTypeInsn(NEW, "java/util/ArrayDeque");
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayDeque", "<init>", "()V", false);
                } else if (isExtendList(internalName) || isExtendMap(internalName) || isExtendSet(internalName) || isExtendQueue(internalName)) {
                    mv.visitTypeInsn(NEW, internalName);
                    mv.visitInsn(DUP);
                    mv.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", "()V", false);
                } else {
                    mv.visitInsn(ACONST_NULL);
                }
//                mv.visitInsn(ARETURN);
                break;
        }
    }

    private String getClassName(String internalName) {
        //[Ljava/lang/Double;
        if (internalName.lastIndexOf("[") > 0) {
            return internalName.substring(1);
        }
        int index = internalName.indexOf("[L");
        if (index >= 0) {
            internalName = internalName.substring(index + "[L".length());
        }
        if (internalName.endsWith(";")) {
            internalName = internalName.substring(0, internalName.length() - 1);
        }
        return internalName;
    }

    private Class<?> getClass(String internalName) throws ClassNotFoundException {
        String classPackage = internalName.replaceAll("/", ".");
        return Class.forName(classPackage);
    }

    private boolean isBoolean(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return Boolean.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isString(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return String.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isExtendList(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return List.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isList(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return List.class == clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isExtendMap(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return Map.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isMap(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return Map.class == clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isExtendSet(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return Set.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isSet(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return Set.class == clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isExtendQueue(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return Queue.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isQueue(String internalName) {
        try {
            Class clazz = getClass(internalName);
            return Queue.class == clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
