package me.nurio.simplerest;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class HttpConnection extends Thread {

    private Socket connect;
    private String ipAddress;

    public HttpConnection(Socket c) throws IOException {
        connect = c;
        ipAddress = connect.getInetAddress().getHostAddress();
        System.out.println("'" + ipAddress + "' connection started." + "(" + new Date() + ")");
    }

    @Override
    public void run() {
        System.out.println("Accepted HTTP request from " + ipAddress);

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            PrintWriter out = new PrintWriter(connect.getOutputStream());
            BufferedOutputStream dataOut = new BufferedOutputStream(connect.getOutputStream());
            Socket connect = this.connect
        ) {

            String header = in.readLine();
            System.out.println("DEBUG: Input contents for " + ipAddress);
            System.out.println(header);

            StringTokenizer parse = new StringTokenizer(header);
            String method = parse.nextToken().toUpperCase();
            String path = parse.nextToken().toLowerCase();

            HttpRequest request = new HttpRequest(out, dataOut, path, method);
            request.process();

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe.getMessage());
            ioe.printStackTrace();
        }

        System.out.println("'" + ipAddress + "' connection closed.");
    }

}