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
