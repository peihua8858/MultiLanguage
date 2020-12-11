package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

/**
 * 方法重写
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/10 9:30
 */
public interface OverrideMethodVisitor {
    void setNeedAddMethod(boolean isNeedMethod);

    /**
     * 重写方法
     *
     * @param cw
     * @param className
     * @param superClassName
     * @param fileName
     * @return
     */
    boolean methodVisitor(ClassWriter cw, String className, String superClassName, String fileName);

    /**
     * 访问类的方法。 此方法<i>必须</ i>返回一个新的{@link MethodVisitor}
     *     *实例（或{@literal null}）每次都被调用，即它不应返回先前的
     *     *返回的访客。
     *
     * @param classWriter
     * @param className
     * @param superClassName
     * @param overwriteClass
     * @param access
     * @param name
     * @param descriptor
     * @param signature
     * @param exceptions
     * @return
     * @see {@link ClassVisitor#visitMethod(int, String, String, String, String[])}
     */
    MethodVisitor visitMethod(ClassWriter classWriter, String className, String superClassName, List<String> overwriteClass,
                              int access, String name, String descriptor, String signature, String[] exceptions);

    /**
     * 返回当前方法名称
     *
     * @return
     */
    String getMethod();

    boolean isOverrideMethod(String className, String superClassName);

}
