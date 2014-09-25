package com.pancakecode.jas.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.pancakecode.jas.resource.FileResource;
import com.pancakecode.jas.resource.Resource;
import com.pancakecode.jas.resource.UrlResource;

public class ResourceUtils {
    
    /** Pseudo URL prefix for loading from the class path: "classpath:" */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    
    /** URL prefix for loading from the file system: "file:" */
    public static final String FILE_URL_PREFIX = "file:";
    
    /** URL protocol for a file in the file system: "file" */
    public static final String URL_PROTOCOL_FILE = "file";
    
    /** URL protocol for an entry from a jar file: "jar" */
    public static final String URL_PROTOCOL_JAR = "jar";
    
    /** URL protocol for an entry from a zip file: "zip" */
    public static final String URL_PROTOCOL_ZIP = "zip";
    
    /** URL protocol for an entry from a JBoss jar file: "vfszip" */
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";
    
    /** URL protocol for an entry from a WebSphere jar file: "wsjar" */
    public static final String URL_PROTOCOL_WSJAR = "wsjar";
    
    /** URL protocol for an entry from an OC4J jar file: "code-source" */
    public static final String URL_PROTOCOL_CODE_SOURCE = "code-source";
    
    /** Separator between JAR URL and file path within the JAR */
    public static final String JAR_URL_SEPARATOR = "!/";
    
    public static final String CLASS_PATTERN = "**/*.class";
    
    // private static final Log logger = LogFactory.getLog(ResourceUtil.class);
    
    /**
     * Resolve the given resource URI to a <code>java.io.File</code>, i.e. to a
     * file in the file system.
     * 
     * @param resourceUri
     *            the resource URI to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException
     *             if the URL cannot be resolved to a file in the file system
     */
    public static File getFile(URI resourceUri) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(resourceUri
                    + " cannot be resolved to absolute file path "
                    + "because it does not reside in the file system: " + resourceUri);
        }
        return new File(resourceUri.getSchemeSpecificPart());
    }
    
    /**
     * Determine whether the given URL points to a resource in a jar file, that
     * is, has protocol "jar", "zip", "wsjar" or "code-source".
     * <p>
     * "zip" and "wsjar" are used by BEA WebLogic Server and IBM WebSphere,
     * respectively, but can be treated like jar files. The same applies to
     * "code-source" URLs on Oracle OC4J, provided that the path contains a jar
     * separator.
     * 
     * @param url
     *            the URL to check
     * @return whether the URL has been identified as a JAR URL
     */
    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol)
                || URL_PROTOCOL_VFSZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol) || (URL_PROTOCOL_CODE_SOURCE
                .equals(protocol) && url.getPath().indexOf(JAR_URL_SEPARATOR) != -1));
    }
    
    /**
     * Create a URI instance for the given location String, replacing spaces
     * with "%20" quotes first.
     * 
     * @param location
     *            the location String to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException
     *             if the location wasn't a valid URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }
    
    public static Set<Resource> getResourcesByScanPath(String scanPath) throws IOException {
        Enumeration<URL> resourceUrls = ClassUtils.getClassLoader().getResources(scanPath);
        
        Set<Resource> result = new LinkedHashSet<Resource>();
        while (resourceUrls.hasMoreElements()) {
            URL resourceUrl = (URL) resourceUrls.nextElement();
            String urlStr = resourceUrl.toString();
            if (!urlStr.endsWith(".class") && !urlStr.endsWith("/")) {
                resourceUrl = new URL(urlStr + "/");
            }
            
            if (isJarURL(resourceUrl)) {
                Set<UrlResource> classUrls = findJarClasses(resourceUrl);
                
                if (classUrls != null) {
                    result.addAll(classUrls);
                }
            } else {
                Set<FileResource> classFiles = findFileClasses(resourceUrl);
                
                if (classFiles != null) {
                    result.addAll(classFiles);
                }
                
            }
            
        }
        
        return result;
    }
    
    /**
     * Resolve the given jar file URL into a JarFile object.
     */
    private static JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith(FILE_URL_PREFIX)) {
            try {
                return new JarFile(toURI(jarFileUrl).getSchemeSpecificPart());
            } catch (URISyntaxException ex) {
                return new JarFile(jarFileUrl.substring(FILE_URL_PREFIX.length()));
            }
        } else {
            return new JarFile(jarFileUrl);
        }
    }
    
    public static Set<FileResource> findFileClasses(URL rootUrl) throws IOException {
        File rootDir = new File(rootUrl.getFile());
        return retrieveMatchingFiles(rootDir.getAbsoluteFile(), CLASS_PATTERN);
    }
    
    /**
     * Retrieve files that match the given path pattern, checking the given
     * directory and its subdirectories.
     * 
     * @param rootDir
     *            the directory to start from
     * @param pattern
     *            the pattern to match against, relative to the root directory
     * @return the Set of matching File instances
     * @throws IOException
     *             if directory contents could not be retrieved
     */
    private static Set<FileResource> retrieveMatchingFiles(File rootDir, String pattern)
            throws IOException {
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("Resource path [" + rootDir
                    + "] does not denote a directory");
        }
        String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
        if (!pattern.startsWith("/")) {
            fullPattern += "/";
        }
        fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
        Set<FileResource> result = new LinkedHashSet<FileResource>(8);
        doRetrieveMatchingFiles(fullPattern, rootDir, result);
        return result;
    }
    
    /**
     * Recursively retrieve files that match the given pattern, adding them to
     * the given result list.
     * 
     * @param fullPattern
     *            the pattern to match against, with preprended root directory
     *            path
     * @param dir
     *            the current directory
     * @param result
     *            the Set of matching File instances to add to
     * @throws IOException
     *             if directory contents could not be retrieved
     */
    private static void doRetrieveMatchingFiles(String fullPattern, File dir,
            Set<FileResource> result) throws IOException {
        
        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            throw new IOException("Could not retrieve contents of directory ["
                    + dir.getAbsolutePath() + "]");
        }
        for (int i = 0; i < dirContents.length; i++) {
            File content = dirContents[i];
            String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
            if (content.isDirectory() && MatcherUtils.matchStart(fullPattern, currPath + "/")) {
                doRetrieveMatchingFiles(fullPattern, content, result);
            }
            if (MatcherUtils.match(fullPattern, currPath)) {
                result.add(new FileResource(content));
            }
        }
    }
    
    public static Set<UrlResource> findJarClasses(URL resourceUrl) throws IOException {
        URLConnection con = resourceUrl.openConnection();
        JarFile jarFile = null;
        String jarFileUrl = null;
        String rootEntryPath = null;
        boolean newJarFile = false;
        
        if (con instanceof JarURLConnection) {
            // Should usually be the case for traditional JAR files.
            JarURLConnection jarCon = (JarURLConnection) con;
            jarCon.setUseCaches(false);
            jarFile = jarCon.getJarFile();
            jarFileUrl = jarCon.getJarFileURL().toExternalForm();
            JarEntry jarEntry = jarCon.getJarEntry();
            rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
        } else {
            // No JarURLConnection -> need to resort to URL file parsing.
            // We'll assume URLs of the format "jar:path!/entry", with the
            // protocol
            // being arbitrary as long as following the entry format.
            // We'll also handle paths with and without leading "file:" prefix.
            String urlFile = resourceUrl.getFile();
            int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
            if (separatorIndex != -1) {
                jarFileUrl = urlFile.substring(0, separatorIndex);
                rootEntryPath = urlFile.substring(separatorIndex + JAR_URL_SEPARATOR.length());
                jarFile = getJarFile(jarFileUrl);
            } else {
                jarFile = new JarFile(urlFile);
                jarFileUrl = urlFile;
                rootEntryPath = "";
            }
            newJarFile = true;
        }
        
        try {
            if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
                // Root entry path must end with slash to allow for proper
                // matching.
                // The Sun JRE does not return a slash here, but BEA JRockit
                // does.
                rootEntryPath = rootEntryPath + "/";
            }
            Set<UrlResource> result = new LinkedHashSet<UrlResource>(8);
            for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
                JarEntry entry = entries.nextElement();
                String entryPath = entry.getName();
                if (entryPath.startsWith(rootEntryPath)) {
                    String relativePath = entryPath.substring(rootEntryPath.length());
                    
                    if (relativePath.endsWith(".class")) {
                        if (relativePath.startsWith("/")) {
                            relativePath = relativePath.substring(1);
                        }
                        result.add(new UrlResource(new URL(resourceUrl, relativePath)));
                    }
                }
            }
            return result;
        } finally {
            // Close jar file, but only if freshly obtained -
            // not from JarURLConnection, which might cache the file reference.
            if (newJarFile) {
                jarFile.close();
            }
        }
    }
}
