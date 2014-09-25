package com.pancakecode.jas.test.mixed;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pancakecode.jas.callback.AnnotationCallback;
import com.pancakecode.jas.callback.CallbackAnnotation;

// the AnnotationCallback MUST have the @CallbackAnnotation !!!
@CallbackAnnotation
public class ExampleMixedCallback extends TestCase implements AnnotationCallback{

    @Override
    public void atType(Class<?> targetClass, Annotation annotation) {
        System.out.println("ExampleMixedCallback atType, targetClass:" + targetClass.getName());
        
        ExampleMixedAnnotation exampleAnnotation = (ExampleMixedAnnotation)annotation;
        System.out.println("Annotation id:" + exampleAnnotation.id() + "; Annotation name: " + exampleAnnotation.name());
        
        assertTrue(exampleAnnotation.id() == 1);
    }

    @Override
    public void atMethod(Class<?> targetClass, Method method, Annotation annotation) {
        System.out.println("ExampleMixedCallback atMethod, targetClass:" + targetClass.getName() + "; method:" + method.getName());
        
        ExampleMixedAnnotation exampleAnnotation = (ExampleMixedAnnotation)annotation;
        System.out.println("Annotation id:" + exampleAnnotation.id() + "; Annotation name: " + exampleAnnotation.name());
        

        assertTrue(exampleAnnotation.id() == 3);
    }

    @Override
    public void atField(Class<?> targetClass, Field field, Annotation annotation) {
        System.out.println("ExampleMixedCallback atField, targetClass:" + targetClass.getName() + "; field:" + field.getName());
        
        ExampleMixedAnnotation exampleAnnotation = (ExampleMixedAnnotation)annotation;
        System.out.println("Annotation id:" + exampleAnnotation.id() + "; Annotation name: " + exampleAnnotation.name());
        

        assertTrue(exampleAnnotation.id() == 2);
    }

    @Override
    public Class<? extends Annotation> getTypeAnnotation() {
        return ExampleMixedAnnotation.class;
    }

    @Override
    public Class<? extends Annotation> getMethodAnnotation() {
        return ExampleMixedAnnotation.class;
    }

    @Override
    public Class<? extends Annotation> getFieldAnnotation() {
        return ExampleMixedAnnotation.class;
    }
    
}
