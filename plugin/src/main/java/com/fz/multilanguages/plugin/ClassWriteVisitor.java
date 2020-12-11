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
     * @param extension
     * @param fileName
     * @author dingpeihua
     * @date 2020/1/12 14:01
     * @version 1.0
     */
    public static byte[] classWriteVisitor(ClassReader classReader, ILogger logger, PluginExtensionEntity extension, String fileName) {
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        OverrideClassWriteVisitor classWriteVisitor = new OverrideClassWriteVisitor(classWriter, fileName,logger,
                extension);
        classReader.accept(classWriteVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
