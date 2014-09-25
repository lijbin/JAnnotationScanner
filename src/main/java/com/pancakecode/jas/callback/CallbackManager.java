package com.pancakecode.jas.callback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Singleton manager to manage callback
 * 
 * @author <a href="mailto:lijbin1979@163.com">Jianbin Li</a>
 */
public class CallbackManager {
    private volatile static CallbackManager instance;
    
    private Map<String, AnnotationCallback> typeCallbackMap = new ConcurrentHashMap<String, AnnotationCallback>();
    private Map<String, AnnotationCallback> methodCallbackMap = new ConcurrentHashMap<String, AnnotationCallback>();
    private Map<String, AnnotationCallback> fieldCallbackMap = new ConcurrentHashMap<String, AnnotationCallback>();
    
    public static CallbackManager getInstance() {
        if (instance == null) {
            synchronized (CallbackManager.class) {
                if (instance == null) {
                    instance = new CallbackManager();
                }
            }
        }
        
        return instance;
    }
    
    public void putTypeCallback(String annotationBytecodeClassName, AnnotationCallback callback) {
        typeCallbackMap.put(annotationBytecodeClassName, callback);
    }
    
    public AnnotationCallback getTypeCallback(String annotationBytecodeClassName) {
        return typeCallbackMap.get(annotationBytecodeClassName);
    }
    
    public void putMethodCallback(String annotationBytecodeClassName, AnnotationCallback callback) {
        methodCallbackMap.put(annotationBytecodeClassName, callback);
    }
    
    public AnnotationCallback getMethodCallback(String annotationBytecodeClassName) {
        return methodCallbackMap.get(annotationBytecodeClassName);
    }
    
    public void putFieldResolver(String annotationBytecodeClassName, AnnotationCallback resolver) {
        fieldCallbackMap.put(annotationBytecodeClassName, resolver);
    }
    
    public AnnotationCallback getFieldResolver(String annotationBytecodeClassName) {
        return fieldCallbackMap.get(annotationBytecodeClassName);
    }
}
