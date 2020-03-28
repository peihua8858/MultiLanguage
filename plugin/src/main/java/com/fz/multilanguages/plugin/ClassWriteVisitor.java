package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.util.List;

/**
 * 类二进制文件访问
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/12 13:56
 */
public class ClassWriteVisitor {
    /**
     * 类二进制文件访问
     *
     * @param classReader
     * @param logger
     * @param overwriteClass
     * @param fileName
     * @author dingpeihua
     * @date 2020/1/12 14:01
     * @version 1.0
     */
    public static byte[] classWriteVisitor(ClassReader classReader, ILogger logger, List<String> overwriteClass, String fileName) {
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        OverrideClassWriteVisitor classWriteVisitor = new OverrideClassWriteVisitor(classWriter, logger,
                overwriteClass);
        classReader.accept(classWriteVisitor, ClassReader.EXPAND_FRAMES);
        classWriteVisitor.overrideMethod(fileName);
        return classWriter.toByteArray();
    }
}
