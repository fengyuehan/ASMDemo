package com.example.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;

/**
 * author : zhangzf
 * date   : 2021/1/28
 * desc   :
 */
public class DemoPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        /**
         * 获取扩展，配置节点
         */
        ExtensionContainer extension =  project.getExtensions();
        AppExtension appExtension = (AppExtension) extension.findByName("android");
        appExtension.registerTransform(new DemoTransform());

    }
}
