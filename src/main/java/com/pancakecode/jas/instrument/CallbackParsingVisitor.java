package com.pancakecode.jas.instrument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

import com.pancakecode.jas.callback.AnnotationCallback;
import com.pancakecode.jas.callback.CallbackAnnotation;
import com.pancakecode.jas.callback.CallbackManager;
import com.pancakecode.jas.util.ClassUtils;

public class CallbackParsingVisitor extends ClassAdapter {
    
    protected final Log logger = LogFactory.getLog(getClass());
    
    private final static String CALLBACK_ANNOTATION_CLASSNAME = ClassUtils
            .converToBytecodeName(CallbackAnnotation.class.getName());
    
    private String className;
    
    public CallbackParsingVisitor(ClassVisitor cv) {
        super(cv);
    }
    
    @Override
    public void visit(final int version, final int access, final String name,
            final String signature, final String superName, final String[] interfaces) {
        className = name;
        cv.visit(version, access, name, signature, superName, interfaces);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (CALLBACK_ANNOTATION_CLASSNAME.equals(desc)) {
            // this is a AnnotationCallback
            Class<?> callbackClazz;
            try {
                callbackClazz = Class.forName(className.replace("/", "."));
            } catch (ClassNotFoundException e) {
                logger.error("class not found for callback:" + className, e);
                return super.visitAnnotation(desc, visible);
            }
            
            try {
                AnnotationCallback callback = (AnnotationCallback) callbackClazz.newInstance();
                
                if (callback.getTypeAnnotation() != null) {
                    CallbackManager.getInstance()
                            .putTypeCallback(
                                    ClassUtils.converToBytecodeName(callback.getTypeAnnotation()
                                            .getName()),
                                    (AnnotationCallback) callbackClazz.newInstance());
                }
                
                if (callback.getFieldAnnotation() != null) {
                    CallbackManager.getInstance()
                            .putFieldResolver(
                                    ClassUtils.converToBytecodeName(callback.getFieldAnnotation()
                                            .getName()),
                                    (AnnotationCallback) callbackClazz.newInstance());
                }
                
                if (callback.getMethodAnnotation() != null) {
                    CallbackManager.getInstance().putMethodCallback(
                            ClassUtils.converToBytecodeName(callback.getMethodAnnotation()
                                    .getName()), (AnnotationCallback) callbackClazz.newInstance());
                }
            } catch (InstantiationException e) {
                logger.error("can not instantiation callback:" + className, e);
            } catch (IllegalAccessException e) {
                logger.error("can not instantiation callback:" + className, e);
            }
        }
        
        return super.visitAnnotation(desc, visible);
    }
    
}
