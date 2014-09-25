package com.pancakecode.jas.test.simple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pancakecode.jas.callback.AnnotationCallback;
import com.pancakecode.jas.callback.CallbackAnnotation;

@CallbackAnnotation
public class ExampleMethodCallback extends TestCase implements AnnotationCallback {
    
    @Override
    public void atType(Class<?> targetClass, Annotation annotation) {
        // not type annotation callback, do nothing        
    }
    
    @Override
    public void atMethod(Class<?> targetClass, Method method, Annotation annotation) {
        System.out.println("ExampleMethodCallback atMethod, targetClass:" + targetClass.getName() + "; method:" + method.getName());
        
        ExampleMethodAnnotation exampleAnnotation = (ExampleMethodAnnotation)annotation;
        System.out.println("Annotation id:" + exampleAnnotation.id() + "; Annotation name: " + exampleAnnotation.name());
        

        assertTrue(exampleAnnotation.id() == 3);
    }
    
    @Override
    public void atField(Class<?> targetClass, Field field, Annotation annotation) {       
        // not field annotation callback, do nothing        
    }
    
    @Override
    public Class<? extends Annotation> getTypeAnnotation() {
        // not type annotation callback, return null
        return null;
    }
    
    @Override
    public Class<? extends Annotation> getMethodAnnotation() {
        return ExampleMethodAnnotation.class;
    }
    
    @Override
    public Class<? extends Annotation> getFieldAnnotation() {
        // not field annotation callback, return null
        return null;
    }
    
}
