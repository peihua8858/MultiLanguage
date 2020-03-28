package com.fz.multilanguages.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 多语言插件
 */
class MultiLanguagesPlugin implements Plugin<Project> {
    public static final String PLUGIN_NAME = "multiLanguages"

    @Override
    void apply(Project project) {
        project.dependencies {
//            implementation 'com.fz.multilanguages:multi-languages:1.0.5'
        }
        //注册plugin参数插件
        project.extensions.create(PLUGIN_NAME, PluginExtension)
        def pluginExtension = project.extensions.findByName(PLUGIN_NAME) as PluginExtension
        //没有开启返回（默认开启）
        if (!pluginExtension.enable) {
            return
        }
        //注册插桩插件
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new MultiLanguagesTransform(pluginExtension))
    }

}