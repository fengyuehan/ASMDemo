package com.example.plugin;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * author : zhangzf
 * date   : 2021/1/28
 * desc   :
 */
public class DemoClassVisitor extends ClassVisitor {

    public DemoClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int accept, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(accept, name, desc, signature, exceptions);
        if (name.equals("a") && desc.equals("(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Ldji/ux/internal/SlidingDialog$OnEventListener;)Ldji/ux/internal/SlidingDialog;")){
            return new MyMethodVisitor(methodVisitor);
        }
        return methodVisitor;
    }

    /**
     *
     */
    static class MyMethodVisitor extends MethodVisitor{

        public MyMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM5, methodVisitor);
        }

        /**
         *
         * @param opcode 指令
         * @param owner
         * @param name
         * @param desc
         * @param itf
         */
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (name.equals("show")){
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
