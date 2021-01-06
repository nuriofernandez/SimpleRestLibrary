package me.nurio.simplerest;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class HttpConnection extends Thread {

    private Socket connect;
    private String ipAddress;
    private String sessionId;

    public HttpConnection(Socket c) throws IOException {
        connect = c;
        ipAddress = connect.getInetAddress().getHostAddress();
        sessionId = Integer.toHexString(hashCode()).substring(0, 5);
        log("Connection started." + "(" + new Date() + ")");
    }

    @Override
    public void run() {
        log("Accepted HTTP request");

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            PrintWriter out = new PrintWriter(connect.getOutputStream());
            BufferedOutputStream dataOut = new BufferedOutputStream(connect.getOutputStream());
            Socket connect = this.connect
        ) {

            String header = in.readLine();
            log("Request header: " + header);

            StringTokenizer parse = new StringTokenizer(header);
            String method = parse.nextToken().toUpperCase();
            String path = parse.nextToken().toLowerCase();

            HttpRequest request = new HttpRequest(this, out, dataOut, path, method);
            request.process();

        } catch (IOException ioe) {
            log("Server error : " + ioe.getMessage());
            ioe.printStackTrace();
        }

        log("Connection closed");
    }

    public void log(String message) {
        System.out.println("[" + ipAddress + " @ " + sessionId + "] >> " + message);
    }

}