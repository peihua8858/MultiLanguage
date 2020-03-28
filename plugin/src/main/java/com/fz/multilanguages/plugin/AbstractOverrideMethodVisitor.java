package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

/**
 * 基础类
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/10 16:49
 */
public abstract class AbstractOverrideMethodVisitor implements OverrideMethodVisitor {
    protected String methodName;
    protected boolean isDeleteFormerMethod = true;

    @Override
    public MethodVisitor visitMethod(ClassWriter classWriter, String className, String superClassName, List<String> overwriteClass, int access, String name, String descriptor, String signature, String[] exceptions) {
        this.methodName = name;
        if (equalsMethod(name) && ConfigsMethod.needAddOrOverride(className, superClassName)) {
            if (ConfigsMethod.isOverwriteAttachMethod(overwriteClass, className)) {
                isDeleteFormerMethod = true;
                //删除原有 attachBaseContext 方法
                return null;
            } else {
                isDeleteFormerMethod = false;
            }
        }
        return classWriter.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public boolean isOverrideMethod(String className, String superClassName) {
        return isDeleteFormerMethod;
    }

    /**
     * @param methodName
     * @return
     * @author dingpeihua
     * @date 2020/1/10 17:01
     * @version 1.0
     */
    protected abstract boolean equalsMethod(String methodName);

    @Override
    public String getMethod() {
        return methodName;
    }

}
