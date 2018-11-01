package WebServer.server;

import WebServer.config.ServerConfig;

import java.io.*;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.GregorianCalendar;

public class ServerUtils {

    static ServerConfig serverConfig = ServerConfig.getNewInstance();

    /**
     * @param fileName
     * @return response content type
     */
    public static String getContentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else {
            return "plain/text";
        }
    }


    /**
     * send response page to user request
     *
     * @param printWriter
     * @param bufferedOutputStream
     * @param requestedFile
     */
    public static void showRequestedPage(PrintWriter printWriter, BufferedOutputStream bufferedOutputStream, String requestedFile) {

        File file = null;

        //remove http get parameters that started whit "?" to prevent 404 error
        requestedFile = cleanUrlRequestedFile(requestedFile);
        if (requestedFile.trim().equals("/") || requestedFile.equals("") || requestedFile.equals(null)) {
            file = new File(serverConfig.getRoot() + "index.html");
        } else {
            file = new File(serverConfig.getRoot() + requestedFile);
        }
        //check requested file exists
        if (file.exists()) {
            int fileLength = (int) file.length();
            byte[] data = readFileBytes(file, fileLength);

            //response status
            printWriter.println("HTTP/1.0 200 OK");

            //response content type
            printWriter.println("Content-Type: text/html");
            //send server name
            printWriter.println("Server: JavaServer");

            //separate headers and content
            printWriter.println("");

            printWriter.flush();
            try {
                //write and send html file to request
                bufferedOutputStream.write(data, 0, fileLength);
                bufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            if (checkStaticsDirEmpty()){
                file = new File(serverConfig.getRoot()+"server_files\\" + serverConfig.getDefaultPage());
                int fileLength = (int) file.length();
                byte[] data = readFileBytes(file, fileLength);

                //response status
                printWriter.println("HTTP/1.0 200 OK");

                //response content type
                printWriter.println("Content-Type: text/html");
                //send server name
                printWriter.println("Server: JavaServer");

                //separate headers and content
                printWriter.println("");

                printWriter.flush();
                try {
                    //write and send html file to request
                    bufferedOutputStream.write(data, 0, fileLength);
                    bufferedOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //send 404.html
                showNotFoundPage(printWriter, bufferedOutputStream, requestedFile);
            }
        }

        if (serverConfig.isDebug()) {
            //show request log in console
            System.out.println("File " + file.getName() + " requested in " + new GregorianCalendar().getTime());
        }
    }

    /**
     * read html file bytes
     *
     * @param file
     * @param fileLength
     * @return requested file bytes[]
     */
    public static byte[] readFileBytes(File file, int fileLength) {
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[fileLength];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    /**
     * show 404 error page
     *
     * @param printWriter
     * @param outputStream
     * @param requestedFile
     */
    public static void showNotFoundPage(PrintWriter printWriter, OutputStream outputStream, String requestedFile) {
        File file = new File(serverConfig.getRoot() + "server_files\\" + serverConfig.getNotFoundPage());
        if (serverConfig.isDebug()) {
            System.err.println("not found 404");
        }


        int fileLength = (int) file.length();
        byte[] data = ServerUtils.readFileBytes(file, fileLength);

        //send 404 error and not found error
        printWriter.println("HTTP/1.0 404 invalid");


        printWriter.println("Content-Type: text/html");
        printWriter.println("Server: JavaServer");

        //separate headers and content
        printWriter.println("");

        printWriter.flush();
        try {
            outputStream.write(data, 0, fileLength);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * invalid HTTP request method
     *
     * @param printWriter
     * @param bufferedOutputStream
     */
    public static void showInvalidMethodPage(PrintWriter printWriter, BufferedOutputStream bufferedOutputStream) {
        if (serverConfig.isDebug()) {
            System.err.println("method is invalid or not supported !!!");
        }
        File file = new File(serverConfig.getRoot() + "server_files\\" + serverConfig.getInvalidMethodPage());
        int fileLength = (int) file.length();
        String mimeType = "text/html";

        byte[] data = ServerUtils.readFileBytes(file, fileLength);

        //(HTTP) 501 Not Implemented server error response code indicates
        // that the request method is not supported by the server and cannot be handled
        printWriter.println("HTTP/1.1 501 Not Implemented");
        printWriter.println(String.format("%s %s", "Content-type: ", mimeType));
        printWriter.println("Server: JavaServer");

        printWriter.println(String.format("%s %d", "Content-length: ", fileLength));
        printWriter.println("");
        printWriter.flush();
        try {
            bufferedOutputStream.write(data, 0, fileLength);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * remove get method parameters to prevent {404} not found page error
     *
     * @param requestedFile
     * @return requested file name without http parameters
     */
    public static String cleanUrlRequestedFile(String requestedFile) {
        if (requestedFile.contains("?")) {
            int index = requestedFile.indexOf("?");
            requestedFile = requestedFile.substring(0, index);
        }
        return requestedFile;
    }

    /**
     * @return boolean
     * @if statics dir is empty webserver shows default page to show webserver works
     */
    public static boolean checkStaticsDirEmpty() {
        boolean check = true;
        String statics = "";
        Path path = Paths.get("statics");
        DirectoryStream<Path> stream = null;
        try {
            stream = Files.newDirectoryStream(path);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        for (Path entry : stream) {
            if (entry.getFileName().toString().contains(".")) {
                check = false;
            }
        }
        return check;
    }
}
