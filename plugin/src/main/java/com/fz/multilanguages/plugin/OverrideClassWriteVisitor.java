package com.fz.multilanguages.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final HashMap<String, OverrideMethodVisitor> methodVisitors = new HashMap<>();
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
    private String fileName;

    public OverrideClassWriteVisitor(ClassWriter cv, String fileName, ILogger logger, PluginExtensionEntity extension) {
        super(Opcodes.ASM7, cv);
        this.fileName = fileName;
        this.extension = extension;
        this.overwriteClass = extension.getOverwriteClass();
        this.classWriter = cv;
        this.logger = logger;
        this.hookPoint = extension.getHookPoint();
        Set<Map.Entry<String, Class<? extends OverrideMethodVisitor>>> methodVisitors = ConfigsMethod.METHOD_VISITOR.entrySet();
        OverrideMethodVisitor methodVisitor = null;
        for (Map.Entry<String, Class<? extends OverrideMethodVisitor>> entry : methodVisitors) {
            final Class<? extends OverrideMethodVisitor> clazz = entry.getValue();
            final String method = entry.getKey();
            try {
                methodVisitor = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (methodVisitor != null) {
                this.methodVisitors.put(method, methodVisitor);
            }
        }
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
    public void visitEnd() {
        if (ConfigsMethod.needAddOrOverride(mClassName, superClassName, extension)) {
            Set<Map.Entry<String, OverrideMethodVisitor>> addEntries = methodVisitors.entrySet();
            for (Map.Entry<String, OverrideMethodVisitor> entry : addEntries) {
                final OverrideMethodVisitor methodVisitor = entry.getValue();
                final String method = entry.getKey();
                final boolean isOverrideMethod = methodVisitor.isOverrideMethod(mClassName, superClassName);
                logger.printlnLog("start override  method " + method + " to " + fileName + ",isOverrideMethod " + isOverrideMethod);
                if (isOverrideMethod) {
                    boolean result = methodVisitor.methodVisitor(classWriter, mClassName, superClassName, fileName);
                    logger.printlnLog("override method " + method + " to " + fileName + (result ? " success." : " failure."));
                } else {
                    slf4jLogger.error("skip " + fileName + ", you should add " + entry.getKey() + " by your self, " +
                            "or you can add this full class name to plugin extension.overwriteClass");
                }
            }
        }
        super.visitEnd();
    }

    public boolean hasMethod(String methodName) {
        return methodVisitors.containsKey(methodName);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {
        if (access == Opcodes.ACC_NATIVE) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        if (ConfigsMethod.needAddOrOverride(mClassName, superClassName, extension) && hasMethod(name)) {
            try {
                logger.printlnLog("visitMethod className>" + mClassName + " Method name>"
                        + name);
                OverrideMethodVisitor overrideMethodVisitor = methodVisitors.get(name);
                overrideMethodVisitor.setNeedAddMethod(true);
                return overrideMethodVisitor.visitMethod(classWriter, mClassName, superClassName, overwriteClass,
                        access, name, descriptor, signature, exceptions);
            } catch (Exception e) {
                e.printStackTrace();
                slf4jLogger.error("visitMethod className>" + mClassName + "  method name> " + name + " to " + fileName + "failure, error:" + e.getMessage());
            }
        }
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ((hookMethod.contains(name) || hasHookMethodClass)) {
            logger.printlnLog("start hook className>" + mClassName + " Method name>" + name + ",hasHookMethodClass:" + hasHookMethodClass);
            return new TryCatchHookAdapter(methodVisitor, mClassName, access, name, descriptor,
                    exceptionHandleClass, exceptionHandleMethod);
        } else {
            return methodVisitor;
        }
    }
}
