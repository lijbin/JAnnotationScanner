package com.pancakecode.jas.util;

public class ClassUtils {
    public static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        
        if (cl != null) {
            return cl;
        } else {
            return ResourceUtils.class.getClassLoader();
        }
    }
    
    /**
     * convert class name (eg, java.lang.String) to byte code name (eg, Ljava/lang/String;)
     * @param className
     * @return
     */
    public static String converToBytecodeName(String className) {
        if(className == null) {
            return null;
        }
        
        return "L" + className.replace(".", "/") + ";";
    }
    
    
}
