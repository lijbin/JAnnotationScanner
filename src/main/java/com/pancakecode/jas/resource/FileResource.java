package com.pancakecode.jas.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileResource implements Resource {
    private final File file;
    
    public FileResource(File file) {
        this.file = file;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
    
    @Override
    public URL getURL() throws IOException {
        return this.file.toURI().toURL();
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj == this || (obj instanceof FileResource && this.file.getAbsolutePath().equals(
                ((FileResource) obj).file.getAbsolutePath())));
    }
    
    @Override
    public int hashCode() {
        return this.file.hashCode();
    }
    
    @Override
    public String toString() {
        return this.file.getAbsolutePath();
    }
    
}
