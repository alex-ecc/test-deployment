package de.ecc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class Main {

    private static HttpServer SERVER;

    public static void main(String[] args) {
        try {
            int port;
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            } else {
                port = 8400;
            }
            SERVER = HttpServer.create(new InetSocketAddress(port), 0);
            SERVER.createContext("/", new BasicHandler());
            SERVER.setExecutor(null);
            System.out.println("Started server listening on port " + port);
            SERVER.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Stopping server now ...");
                long started = System.currentTimeMillis();
                SERVER.stop(1);
                System.out.println("... done in " + (System.currentTimeMillis() - started) + "ms.");
            }));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    static class BasicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("[" + Instant.now() + "] Got server request.");
            String response = "Hallo Welt!";
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.getResponseHeaders().add("Server", "Not Apache");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes(StandardCharsets.UTF_8));
            responseBody.close();
        }
    }
}