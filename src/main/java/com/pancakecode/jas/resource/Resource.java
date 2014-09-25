package com.pancakecode.jas.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface Resource {
    InputStream getInputStream() throws IOException;
    URL getURL() throws IOException;
}
