package com.pancakecode.jas.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlResource implements Resource {
    
    private final URL url;
    
    public UrlResource(URL url) {
        this.url = url;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        con.setUseCaches(false);
        return con.getInputStream();
    }
    
    @Override
    public URL getURL() throws IOException {
        return this.url;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj == this || (obj instanceof UrlResource && this.url
                .equals(((UrlResource) obj).url)));
    }
    
    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
    
    @Override
    public String toString() {
        return this.url.toString();
    }
}
