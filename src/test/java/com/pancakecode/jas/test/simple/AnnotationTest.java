package com.pancakecode.jas.test.simple;

import junit.framework.TestCase;

import com.pancakecode.jas.AnnotationScanner;

public class AnnotationTest extends TestCase{
    
    public void test() throws Exception { 
        AnnotationScanner.scan("com.pancakecode.jas.test.simple");

    }
}
