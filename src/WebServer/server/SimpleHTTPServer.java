package WebServer.server;

import WebServer.config.ServerConfig;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class SimpleHTTPServer implements Runnable {


    //reading project config by ServerConfig
    private ServerConfig serverConfig;

    private Socket connection;


    ServerSocket serverSocket;

    /**
     * @throws IOException
     */
    public SimpleHTTPServer() throws IOException {
        serverConfig = ServerConfig.getNewInstance();
    }


    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //show server logs on console
        if (serverConfig.isDebug()) {
            System.out.println(" server created ... \n");
            System.out.println("connection starting up on port " + serverConfig.getPort());
        }
        listen();
    }

    /**
     * listen to requests and send response
     */
    private void listen() {

        for (; ; ) {

            try {
                serverSocket = new ServerSocket(serverConfig.getPort());
                connection = serverSocket.accept();

                connection.setKeepAlive(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //read connection input stream
            BufferedReader bufferedReader = null;

            //write http headers such as Content-Type etc.
            PrintWriter printWriter = null;

            //send response to browser
            BufferedOutputStream bufferedOutputStream = null;

            //read requested page file name by StringTokenizer
            String requestedFile;

            String line = ".";
            try {
                //create new print write to write and send http headers
                printWriter = new PrintWriter(connection.getOutputStream());

                //read request attributes such as method and etc. by bufferedReader
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //send html files response by bufferedOutputStream
                bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());

                line = bufferedReader.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //tokenize request stream such as request method , page requested etc.
            StringTokenizer parse = new StringTokenizer(line);

            //get request method
            String method = parse.nextToken().toUpperCase();

            //get file requested name
            requestedFile = parse.nextToken().toLowerCase();

            if (!(method.equals("GET")) && !(method.equals("POST"))) {
                ServerUtils.showInvalidMethodPage(printWriter, bufferedOutputStream);
            } else {
                if (method.equals("GET") || method.equals("POST")) {
                    ServerUtils.showRequestedPage(printWriter, bufferedOutputStream, requestedFile);
                }
            }

            try {
                connection.close();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
