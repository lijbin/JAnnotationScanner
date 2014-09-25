# JAnnotationScanner: Java Annotation Scanner

JAnnotationScanner provides a callback mechanism when scanning the Annotations against 
the specified class package.

When the scanner find the target class which contains annotations, it will invoke the
corresponding callback for the annotations.

The full example can see: src/test/java/com.pancakecode.jas.test.mixed

JAnnotationScanner runs in two modes: 

- STATIC: in your code, simply invoke AnnotationScanner.scan like this:
			// make sure the annotations and callback are defined in "your.package" 
			AnnotationScanner.scan("your.package"); 

- JAVAAGENT: make use of the javaagent mechanism and instrument the scanner without writing any code:
			in your java running cmd, add java options like this:
			java -javaagent:/your/localpath/jas-1.0.0.jar="-Syour.package"
			
			In my laptop, it looks like this:
			java -javaagent:C:\Users\lijianbin\workspace\JAnnotationScanner\target\jas-1.0.0.jar="-Scom.pancakecode"
			
### Dependencies

- [ASM](http://asm.ow2.org/)

### Maintainers

- Jianbin Li (lijbin1979@163.com)			
	