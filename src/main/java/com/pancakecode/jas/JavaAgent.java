package com.pancakecode.jas;

import java.lang.instrument.Instrumentation;

public class JavaAgent {
    
    private final static String ARGUMENT_SEPERATOR = " ";
    
    /**
     * Entry method for java agent instrumentation
     * 
     * @param args
     * @param instrumentation
     * @throws Exception
     */
    public static void premain(String args, Instrumentation instrumentation) throws Exception {        
        String scanPath = null;
        if (args != null) {
            for (String arg : args.split(ARGUMENT_SEPERATOR)) {
                if ((arg.length() < 2) || (arg.charAt(0) != '-')) {
                    throw new IllegalArgumentException(arg);
                }
                
                final String value = (arg.length() > 2) ? arg.substring(2) : null;
                
                switch (arg.charAt(1)) {
                case 'S':
                    // 'S' means (scan path)
                    scanPath = value;
                    break;
                default:
                    
                    throw new IllegalArgumentException(args);
                    
                }
            }
            
        }
        
        AnnotationScanner.scan(scanPath);
    }
    
}
