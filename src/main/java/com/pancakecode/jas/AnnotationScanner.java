package com.pancakecode.jas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.pancakecode.jas.instrument.AnnotationParsingVisitor;
import com.pancakecode.jas.instrument.CallbackParsingVisitor;
import com.pancakecode.jas.resource.Resource;
import com.pancakecode.jas.util.ResourceUtils;

/**
 * Class provided a static method to scan package to find annotations 
 * and the corresponding callback.
 *
 * @author <a href="mailto:lijbin1979@163.com">Jianbin Li</a>
 */
public class AnnotationScanner {
    private static Log logger = LogFactory.getLog(AnnotationScanner.class);
    
    private final static String PATH_SEPERATOR = ";";
    
    /**
     * To track the parsed classes
     */
    private static Set<String> parsedClasses = new HashSet<String>();
    
    public static boolean isClassParsed(String className) {
        return parsedClasses.contains(className);
    }
    
    public static void putParsedClass(String className) {
        parsedClasses.add(className);
    }
    
    /**
     *  scan package to find annotations and the corresponding callback.
     * 
     * @param scanPaths package path to scan, for example, "com.test"
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static synchronized void scan(String scanPaths)
            throws IOException, ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        if (scanPaths == null) {
            return;
        }
        
        Set<Resource> scannedResources = new HashSet<Resource>();
        
        // replace path from '.' to '/' before splitting
        for (String scanPath : scanPaths.replace(".", "/").split(PATH_SEPERATOR)) {
            Set<Resource> resources = ResourceUtils.getResourcesByScanPath(scanPath);
            
            if (resources != null) {
                scannedResources.addAll(resources);
            }
        }
        
        // parse the callback 
        for (Resource resource : scannedResources) {
            InputStream is = null;
            try {
                is = resource.getInputStream();
            } catch (FileNotFoundException e) {
                logger.error("file not found", e);
                continue;
            }
            
            try {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                CallbackParsingVisitor adapter = new CallbackParsingVisitor(writer);
                ClassReader reader = new ClassReader(is);
                reader.accept(adapter, 0);
            } catch(Exception e) {
                logger.error("parsing callback error", e);
            }
        }
        
        // parse the annotations 
        for (Resource resource : scannedResources) {
            InputStream is = null;
            try {
                is = resource.getInputStream();
            } catch (FileNotFoundException e) {
                logger.error("file not found", e);
                continue;
            }
            
            try {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                AnnotationParsingVisitor adapter = new AnnotationParsingVisitor(writer);
                ClassReader reader = new ClassReader(is);
                reader.accept(adapter, 0);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
            
        }
        
        // let gc goes
        parsedClasses.clear();
        
    }
    
}
