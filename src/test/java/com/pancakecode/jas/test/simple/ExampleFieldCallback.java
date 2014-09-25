package com.pancakecode.jas.test.simple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pancakecode.jas.callback.AnnotationCallback;
import com.pancakecode.jas.callback.CallbackAnnotation;

@CallbackAnnotation
public class ExampleFieldCallback extends TestCase implements AnnotationCallback {
    
    @Override
    public void atType(Class<?> targetClass, Annotation annotation) {
        // not type annotation callback, do nothing        
    }
    
    @Override
    public void atMethod(Class<?> targetClass, Method method, Annotation annotation) {
        // not method annotation callback, do nothing
    }
    
    @Override
    public void atField(Class<?> targetClass, Field field, Annotation annotation) {       

        System.out.println("ExampleFieldCallback atField, targetClass:" + targetClass.getName() + "; field:" + field.getName());
        
        ExampleFieldAnnotation exampleAnnotation = (ExampleFieldAnnotation)annotation;
        System.out.println("Annotation id:" + exampleAnnotation.id() + "; Annotation name: " + exampleAnnotation.name());
        

        assertTrue(exampleAnnotation.id() == 2);
    }
    
    @Override
    public Class<? extends Annotation> getTypeAnnotation() {
        // not type annotation callback, return null
        return null;
    }
    
    @Override
    public Class<? extends Annotation> getMethodAnnotation() {
        // not method annotation callback, return null
        return null;
    }
    
    @Override
    public Class<? extends Annotation> getFieldAnnotation() {
        // not field annotation callback, return null
        return ExampleFieldAnnotation.class;
    }
    
}
