package com.pancakecode.jas.instrument;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

import com.pancakecode.jas.AnnotationScanner;
import com.pancakecode.jas.callback.AnnotationCallback;
import com.pancakecode.jas.callback.CallbackManager;
import com.pancakecode.jas.util.ClassUtils;

public class AnnotationParsingVisitor extends ClassAdapter {
    
    protected final Log logger = LogFactory.getLog(getClass());
    
    private Class<?> clazz;
    private String className;
    
    /**
     * avoid parsing a class multiple times, especially there are multiple class
     * files have the same class name
     */
    private boolean isParsedBefore = false;
    
    public AnnotationParsingVisitor(ClassVisitor cv) {
        super(cv);
        
    }
    
    private void parseFieldAnnotations() {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations == null) {
                continue;
            }
            
            for (Annotation annotation : annotations) {
                String annotationDesc = ClassUtils.converToBytecodeName(annotation.annotationType()
                        .getName());
                
                AnnotationCallback callback = CallbackManager.getInstance().getFieldResolver(
                        annotationDesc);
                
                if (callback != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("parseFieldAnnotation: callback found for class:" + className);
                    }
                    if (clazz != null) {
                        callback.atField(clazz, field,
                                field.getAnnotation(callback.getFieldAnnotation()));
                    }
                }
            }
        }
    }
    
    private void parseMethodAnnotations() {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            if (annotations == null) {
                continue;
            }
            
            for (Annotation annotation : annotations) {
                String annotationDesc = ClassUtils.converToBytecodeName(annotation.annotationType()
                        .getName());
                
                AnnotationCallback callback = CallbackManager.getInstance().getMethodCallback(
                        annotationDesc);
                
                if (callback != null) {
                    if (clazz != null) {
                        callback.atMethod(clazz, method,
                                method.getAnnotation(callback.getMethodAnnotation()));
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(final int version, final int access, final String name,
            final String signature, final String superName, final String[] interfaces) {
        className = name;
        
        isParsedBefore = AnnotationScanner.isClassParsed(className);
        if (!isParsedBefore) {
            try {
                clazz = Class.forName(name.replace("/", "."));
                
                AnnotationScanner.putParsedClass(className);
                
                parseFieldAnnotations();
                parseMethodAnnotations();
                
            } catch (ClassNotFoundException e) {
                logger.error(e);
            }
        }
        cv.visit(version, access, name, signature, superName, interfaces);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (!isParsedBefore) {
            AnnotationCallback callback = CallbackManager.getInstance().getTypeCallback(desc);
            if (callback != null) {
                if (clazz != null) {
                    callback.atType(clazz, clazz.getAnnotation(callback.getTypeAnnotation()));
                }
            }
        }
        
        return super.visitAnnotation(desc, visible);
    }
    
}
