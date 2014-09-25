package com.pancakecode.jas.test.simple;


@ExampleTypeAnnotation(id = 1, name = "type annotation")
public class ExampleTargetClass {
    
    @ExampleFieldAnnotation(id = 2, name = "field annotation")
    public String str = "abc";
    
    @ExampleMethodAnnotation(id = 3, name = "method annotation")
    public void setStr(String str) {
        this.str = str;
    }
}
