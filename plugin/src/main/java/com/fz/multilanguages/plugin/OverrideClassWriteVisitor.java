package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
    private String mClassName;
    private List<String> overwriteClass;
    private ClassWriter classWriter;
    private ILogger logger;
    private static Logger slf4jLogger = LoggerFactory.getLogger("logger");
    private String exceptionHandleClass;
    private String exceptionHandleMethod;
    private Map<String, List<String>> hookPoint;
    private List<String> hookMethod;
    private boolean hasHookMethodClass = false;
    private PluginExtensionEntity extension;

    public OverrideClassWriteVisitor(ClassWriter cv, ILogger logger, PluginExtensionEntity extension) {
        super(Opcodes.ASM7, cv);
        this.extension = extension;
        this.overwriteClass = extension.getOverwriteClass();
        this.classWriter = cv;
        this.logger = logger;
        this.hookPoint = extension.getHookPoint();
        Map<String, String> exceptionHandler = extension.getExceptionHandler();
        if (exceptionHandler != null && !exceptionHandler.isEmpty()) {
            exceptionHandler.entrySet().forEach(entry -> {
                exceptionHandleClass = entry.getKey().replace(".", "/");
                exceptionHandleMethod = entry.getValue();
            });
        }
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName,
                      String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        superClassName = superName;
        this.mClassName = name;
        String className = name.replace("/", ".");
        hookMethod = isNotNull(hookPoint.get(className));
        /**
         * 如果参数为*则表示所以方法增加try catch
         */
        hasHookMethodClass = hookMethod.size() == 1 &&
                hookMethod.contains("*") && hookPoint.containsKey(className);
    }

    <T> List<T> isNotNull(List<T> t) {
        if (t == null) {
            return new ArrayList<>();
        }
        return t;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {
        if (access == Opcodes.ACC_NATIVE) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        logger.printlnLog("start visitMethod className>" + mClassName + " Method name>"
                + name + ",hasHookMethodClass:" + hasHookMethodClass);
        if ((hookMethod.contains(name) || hasHookMethodClass)) {
            logger.printlnLog("start hook className>" + mClassName + " Method name>" + name);
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (exceptionHandleClass != null && exceptionHandleMethod != null) {
                return new AddHandleTryCatchMethodVisitor(mv, mClassName, access, name, descriptor,
                        exceptionHandleClass, exceptionHandleMethod);
            }
            return new AddDynamicTryCatchMethodVisitor(mv, mClassName, access, name, descriptor);
        } else if (ConfigsMethod.needAddOrOverride(mClassName, superClassName, extension) && ConfigsMethod.hasMethod(name)) {
            try {
                OverrideMethodVisitor overrideMethodVisitor = ConfigsMethod.OVERRIDE_METHOD_VISITOR.get(name);
                return overrideMethodVisitor.visitMethod(classWriter, mClassName, superClassName, overwriteClass,
                        access, name, descriptor, signature, exceptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public boolean overrideMethod(String fileName) {
        if (ConfigsMethod.needAddOrOverride(mClassName, superClassName, extension)) {
            logger.printlnLog("start override method to " + fileName);
            Set<Map.Entry<String, OverrideMethodVisitor>> entries = ConfigsMethod.OVERRIDE_METHOD_VISITOR.entrySet();
            for (Map.Entry<String, OverrideMethodVisitor> entry : entries) {
                OverrideMethodVisitor methodVisitor = entry.getValue();
                if (methodVisitor.isOverrideMethod(mClassName, superClassName)) {
                    boolean result = methodVisitor.methodVisitor(classWriter, mClassName, superClassName, fileName);
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
                if (methodVisitor.isOverrideMethod(mClassName, superClassName)) {
                    boolean result = methodVisitor.methodVisitor(classWriter, mClassName, superClassName, fileName);
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
