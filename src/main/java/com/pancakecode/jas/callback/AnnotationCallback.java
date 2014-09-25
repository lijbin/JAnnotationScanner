package com.pancakecode.jas.callback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  
 * @author <a href="mailto:lijbin1979@163.com">Jianbin Li</a>
 */
public interface AnnotationCallback {
    /**
     * 
     * @return the class of "Type" Annotation, if the resolver is not targeting
     *         at Type, return null
     */
    Class<? extends Annotation> getTypeAnnotation();
    
    /**
     * 
     * @return the class of "Method" Annotation, if the resolver is not
     *         targeting at Method, return null
     */
    Class<? extends Annotation> getMethodAnnotation();
    
    /**
     * 
     * @return the class of "Field" Annotation, if the resolver is not targeting
     *         at Field, return null
     */
    Class<? extends Annotation> getFieldAnnotation();
    
    /**
     * The callback method when scanner find the Type Annotation
     * 
     * @param targetClass
     * @param annotation
     */
    void atType(Class<?> targetClass, Annotation annotation);
    
    /**
     * The callback method when scanner find the Method Annotation
     * 
     * @param targetClass
     * @param annotation
     */
    void atMethod(Class<?> targetClass, Method method, Annotation annotation);
    
    /**
     * The callback method when scanner find the Method Annotation
     * 
     * @param targetClass
     * @param annotation
     */
    void atField(Class<?> targetClass, Field field, Annotation annotation);
}
