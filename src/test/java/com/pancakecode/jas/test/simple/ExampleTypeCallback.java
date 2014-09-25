package com.pancakecode.jas.test.simple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pancakecode.jas.callback.AnnotationCallback;
import com.pancakecode.jas.callback.CallbackAnnotation;

@CallbackAnnotation
public class ExampleTypeCallback extends TestCase implements AnnotationCallback {
    
    @Override
    public void atType(Class<?> targetClass, Annotation annotation) {
        System.out.println("ExampleTypeCallback atType, targetClass:" + targetClass.getName());
        
        ExampleTypeAnnotation exampleAnnotation = (ExampleTypeAnnotation) annotation;
        System.out.println("Annotation id:" + exampleAnnotation.id() + "; Annotation name: "
                + exampleAnnotation.name());
        
    }
    
    @Override
    public void atMethod(Class<?> targetClass, Method method, Annotation annotation) {
        // not method annotation callback, do nothing
    }
    
    @Override
    public void atField(Class<?> targetClass, Field field, Annotation annotation) {        
        // not field annotation callback, do nothing
    }
    
    @Override
    public Class<? extends Annotation> getTypeAnnotation() {
        return ExampleTypeAnnotation.class;
    }
    
    @Override
    public Class<? extends Annotation> getMethodAnnotation() {
        // not method annotation callback, return null
        return null;
    }
    
    @Override
    public Class<? extends Annotation> getFieldAnnotation() {
        // not field annotation callback, return null
        return null;
    }
    
}
