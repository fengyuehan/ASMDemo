package com.example.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;

/**
 * author : zhangzf
 * date   : 2021/1/28
 * desc   :
 */
public class DemoTransform extends Transform {
    @Override
    public String getName() {
        return "DemoTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        outputProvider.deleteAll();
        //消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //引用型输入，无需输出。
        Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs();
        for (TransformInput input:inputs){
            //获取jar包
            Collection<JarInput> jarInputs = input.getJarInputs();
            for (JarInput jarInput:jarInputs){
                String jarName = jarInput.getName();
                if (jarName.contains("com.dji:dji-uxsdk")){
                    //找q类
                    File file = jarInput.getFile();
                    JarFile jarFile = new JarFile(file);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()){
                        JarEntry jarEntry = entries.nextElement();
                        if (jarEntry.getName().equals("dji/ux/c/q.class")){
                            InputStream inputStream = jarFile.getInputStream(jarEntry);
                            byte[] bytes = IOUtils.read(inputStream);
                            inputStream.close();
                            ClassReader cr = new ClassReader(bytes);
                            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                            cr.accept(new DemoClassVisitor(cw),0);
                            byte[] bytes1 = cw.toByteArray();
                            FileOutputStream fos = new FileOutputStream(new File("dji/ux/c/q.class"));
                            fos.write(bytes1);
                            fos.close();
                        }
                    }
                }
            }
            //获取自己代码的class
            Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs();
            for (DirectoryInput directoryInput :directoryInputs){
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                          directoryInput.getContentTypes()
                        , directoryInput.getScopes()
                        , Format.DIRECTORY);
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyDirectory(directoryInput.getFile(), dest);
            }

        }
    }
}
