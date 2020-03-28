package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 方法重写
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/1/10 9:30
 */
public class OverrideClassWriteVisitor extends ClassVisitor implements Opcodes {
    private String superClassName;
    private String className;
    private List<String> overwriteClass;
    private ClassWriter classWriter;
    private ILogger logger;
    private static Logger slf4jLogger = LoggerFactory.getLogger("logger");

    public OverrideClassWriteVisitor(ClassWriter cv, ILogger logger, List<String> overwriteClass) {
        super(Opcodes.ASM7, cv);
        this.overwriteClass = overwriteClass;
        this.classWriter = cv;
        this.logger = logger;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName,
                      String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        superClassName = superName;
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {
        if (ConfigsMethod.needAddOrOverride(className, superClassName) && ConfigsMethod.hasMethod(name)) {
            try {
                OverrideMethodVisitor overrideMethodVisitor = ConfigsMethod.OVERRIDE_METHOD_VISITOR.get(name);
                return overrideMethodVisitor.visitMethod(classWriter, className, superClassName, overwriteClass,
                        access, name, descriptor, signature, exceptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public boolean overrideMethod(String fileName) {
        if (ConfigsMethod.needAddOrOverride(className, superClassName)) {
            logger.printlnLog("start override method to " + fileName);
            Set<Map.Entry<String, OverrideMethodVisitor>> entries = ConfigsMethod.OVERRIDE_METHOD_VISITOR.entrySet();
            for (Map.Entry<String, OverrideMethodVisitor> entry : entries) {
                OverrideMethodVisitor methodVisitor = entry.getValue();
                if (methodVisitor.isOverrideMethod(className, superClassName)) {
                    boolean result = methodVisitor.methodVisitor(classWriter, className, superClassName, fileName);
                    if (result) {
                        logger.printlnLog("override method " + entry.getKey() + " to " + fileName);
                    }
                } else {
                    slf4jLogger.error("skip " + fileName + ", you should overwrite " + entry.getKey() + " by your self, " +
                            "or you can add this full class name to plugin extension.overwriteClass");
                }
            }
            logger.printlnLog("end override method to " + fileName);
            Set<Map.Entry<String, OverrideMethodVisitor>> addEntries = ConfigsMethod.ADD_METHOD_VISITOR.entrySet();
            for (Map.Entry<String, OverrideMethodVisitor> entry : addEntries) {
                OverrideMethodVisitor methodVisitor = entry.getValue();
                if (methodVisitor.isOverrideMethod(className, superClassName)) {
                    boolean result = methodVisitor.methodVisitor(classWriter, className, superClassName, fileName);
                    if (result) {
                        logger.printlnLog("add method " + entry.getKey() + " to " + fileName);
                    }
                } else {
                    slf4jLogger.error("skip " + fileName + ", you should add " + entry.getKey() + " by your self, " +
                            "or you can add this full class name to plugin extension.overwriteClass");
                }
            }
        }
        return true;
    }
}
