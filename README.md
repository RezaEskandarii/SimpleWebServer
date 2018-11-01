# SimpleWebServer
 
##### This project contains a configuration file in the src\WebServer\config\serverConfig.properties .
There are some basic settings in this file.
``` properties
server.port=8080
server.debug-mode=true
server.default-page=default.html
server.not-found-page=404.html
server.invalid-method-page=invalid_method.html
server.root-path=statics\\
```
##### Use the following code to get started

``` java
import WebServer.server.SimpleHTTPServer;

import java.io.IOException;

public class main {

    public static void main(String[] args)   {
        try {
            SimpleHTTPServer httpServer = new SimpleHTTPServer();
            httpServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
