package WebServer.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {

    private static ServerConfig serverConfig = null;

    private ServerConfig() {
    }

    //properties file for read project config
    private static Properties properties = null;

    //check for enabling or disabling debug mode for print errors and results
    private boolean debug;

    //root web static folder
    private String root;

    //404 error page
    private String notFoundPage;

    //default web server page
    private String defaultPage;

    //invalid methods redirection page
    private String invalidMethodPage;

    //web server port
    private int port;

    private static void init() {

    }


    /**
     * read and initial server configuration file
     */
    private static void initProperties() {
        properties = new Properties();
        try {
            properties.load(new FileReader("src\\WebServer\\config\\serverConfig.properties"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


    }

    /**
     * @return new ServerConfig class
     * @singleton
     */
    public static ServerConfig getNewInstance() {
        if (serverConfig == null) {
            serverConfig = new ServerConfig();
        }
        initProperties();
        return serverConfig;
    }

    public boolean isDebug() {
        this.debug = Boolean.parseBoolean(properties.getProperty("server.debug-mode"));
        return debug;
    }

    public String getRoot() {
        this.root = properties.getProperty("server.root-path");
        return root;
    }

    public String getNotFoundPage() {
        this.notFoundPage = properties.getProperty("server.not-found-page");
        return notFoundPage;
    }

    public String getDefaultPage() {
        this.defaultPage = properties.getProperty("server.default-page");
        return defaultPage;
    }

    public int getPort() {
        this.port = Integer.parseInt(properties.getProperty("server.port"));
        return port;
    }

    public String getInvalidMethodPage() {
        this.invalidMethodPage = properties.getProperty("server.invalid-method-page");
        return invalidMethodPage;
    }
}
