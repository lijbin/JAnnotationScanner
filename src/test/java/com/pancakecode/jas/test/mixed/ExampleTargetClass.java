package com.pancakecode.jas.test.mixed;


@ExampleMixedAnnotation(id = 1, name = "type annotation")
public class ExampleTargetClass {

    @ExampleMixedAnnotation(id = 2, name = "field annotation")
    public String str = "abc";

    @ExampleMixedAnnotation(id = 3, name = "method annotation")
    public void setStr(String str) {
        this.str = str;
    }
}
