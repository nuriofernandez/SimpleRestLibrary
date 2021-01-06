package me.nurio.simplerest;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.StringTokenizer;

public class HttpServer extends Thread {

    private Socket connect;

    private BufferedReader in;
    private PrintWriter out;
    private BufferedOutputStream dataOut;

    private String path;
    private String method;

    public HttpServer(Socket c) throws IOException {
        System.out.println("Started thread.");
        connect = c;
    }

    @Override
    public void run() {
        System.out.println("Started run() ✨.");

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            path = parse.nextToken().toLowerCase();

            process();
        } catch (FileNotFoundException fnfe) {
            System.err.println("Error with file not found exception : " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {

            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            System.out.println("Connection closed.");
        }
    }

    public void process() throws IOException {
        System.out.println("Requested method: " + method);
        System.out.println("Requested path: " + path);

        String body = "Works!";
        String content = "text/html";
        byte[] fileData = body.getBytes(StandardCharsets.UTF_8);
        int fileLength = fileData.length;

        // send HTTP Headers
        out.println("HTTP/1.1 200 OK");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
    }

}