package com.fz.multilanguages.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class MultiLanguagesTransform extends Transform implements ILogger, Plugin<Project> {
    public static final String PLUGIN_NAME = "multiLanguages"
    private PluginExtension pluginExtension
//
//    MultiLanguagesTransform() {
//    }
//
//    MultiLanguagesTransform(PluginExtension pluginExtension) {
//        this.pluginExtension = pluginExtension
//    }

    @Override
    void apply(Project project) {
        project.dependencies {
            implementation 'com.fz.multilanguages:multi-languages:1.0.6'
        }
        //注册plugin参数插件
        def pluginExtension = project.extensions.findByType(PluginExtension)
        if (pluginExtension == null) {
            project.extensions.create(PLUGIN_NAME, PluginExtension)
            pluginExtension = project.extensions.findByType(PluginExtension)
        }
        //没有开启返回（默认开启）
        if (!pluginExtension.enable) {
            return
        }
        this.pluginExtension = pluginExtension
        //注册插桩插件
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    String getName() {
        return 'MultiLanguages'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException,
            InterruptedException, IOException {
        super.transform(transformInvocation)
        if (!pluginExtension.enable) {
            return
        }
        println("+-----------------------------------------------------------------------------+")
        println("|                     Multi Languages Plugin START                            |")
        println("+-----------------------------------------------------------------------------+")
        def startTime = System.currentTimeMillis()
        def inputs = transformInvocation.inputs
        boolean isIncremental = transformInvocation.incremental
        def outputProvider = transformInvocation.outputProvider
        //删除之前的输出
        if (outputProvider != null && !isIncremental) {
            outputProvider.deleteAll()
        }

        inputs.each { TransformInput input ->
            //遍历directoryInputs
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //处理DirectoryInput
                handleDirectoryInput(directoryInput, isIncremental, outputProvider)
            }
            //遍历jarInputs
            input.jarInputs.each { JarInput jarInput ->
                //处理JarInput
                handleJarInput(jarInput, isIncremental, outputProvider)
            }
        }
        //
        def cost = (System.currentTimeMillis() - startTime) / 1000
        println("+-----------------------------------------------------------------------------+")
        println("|                          Multi Languages Plugin END                         |")
        println("|                            Plugin cost ： $cost s                           |")
        println("+-----------------------------------------------------------------------------+")
    }
    /**
     * 处理DirectoryInput
     * @param directoryInput
     * @param outputProvider
     */
    void handleDirectoryInput(DirectoryInput directoryInput, boolean isIncremental, TransformOutputProvider outputProvider) {
        //是否是目录
        if (directoryInput.file.isDirectory()) {
            Map<File, Status> changed = directoryInput.getChangedFiles()
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                boolean doXForm = !isIncremental
                if (changed != null) {
                    Status status = changed.get(file)
                    //(status == null || status == Status.NOTCHANGED || status == Status.REMOVED) 不用处理
                    doXForm |= (status == Status.CHANGED || status == Status.ADDED)
                }
                if (checkClassFile(name) && doXForm) {
                    def classReader = new ClassReader(file.bytes)
                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(ClassWriteVisitor.classWriteVisitor(classReader, this, createProperties(), name))
                    fos.close()
                }
            }
        }
        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)
        //处理完输入文件之后，要把输出给下一个任务
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    PluginExtensionEntity createProperties() {
        PluginExtensionEntity extension = new PluginExtensionEntity()
        extension.setEnable(pluginExtension.enable)
        extension.setHookPoint(pluginExtension.hookPoint)
        extension.setExceptionHandler(pluginExtension.exceptionHandler)
        extension.setOverwriteClass(pluginExtension.overwriteClass)
        extension.setEnableApplication(pluginExtension.enableApplication)
        extension.setEnableIntentService(pluginExtension.enableIntentService)
        extension.setEnableService(pluginExtension.enableService)
        return extension
    }
    /**
     * 处理JarInput
     * @param jarInput
     * @param outputProvider
     */
    void handleJarInput(JarInput jarInput, boolean isIncremental, TransformOutputProvider outputProvider) {
        boolean doXForm = (!isIncremental || Status.ADDED == jarInput.getStatus() || Status.CHANGED == jarInput.getStatus())
        if (doXForm) {
            if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
                //重名名输出文件,因为可能同名,会覆盖
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def jarFile = new JarFile(jarInput.file)
                def enumeration = jarFile.entries()
                def tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
                if (tmpFile.exists()) {
                    tmpFile.delete()
                }
                //
                def jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
                while (enumeration.hasMoreElements()) {
                    def jarEntry = (JarEntry) enumeration.nextElement()
                    def entryName = jarEntry.name
                    def zipEntry = new ZipEntry(entryName)
                    def inputStream = jarFile.getInputStream(jarEntry)
                    //插桩class
                    if (checkClassFile(entryName)) {
                        jarOutputStream.putNextEntry(zipEntry)
                        def classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                        jarOutputStream.write(ClassWriteVisitor.classWriteVisitor(classReader, this, createProperties(), entryName))
                    } else {
                        jarOutputStream.putNextEntry(zipEntry)
                        jarOutputStream.write(IOUtils.toByteArray(inputStream))
                    }
                    jarOutputStream.closeEntry()
                }

                //结束
                jarOutputStream.close()
                jarFile.close()
                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(tmpFile, dest)
                tmpFile.delete()
            }
            return
        }
        if (Status.REMOVED == jarInput.getStatus()) {
            FileUtils.deleteQuietly(outDir)
        }
    }

    /**
     * 检查文件是否为需要处理的问题
     * @param name
     * @return
     */
    boolean checkClassFile(String name) {
        //只处理需要的class文件
        return (name.endsWith(".class")
                && !name.startsWith("R\$")
                && "R.class" != name
                && "BuildConfig.class" != name
                && !name.startsWith("android/support")
                && !name.startsWith("androidx/"))
    }

    @Override
    void printlnLog(Object value) {
        println(value)
    }

}