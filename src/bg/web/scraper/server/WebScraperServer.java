package bg.web.scraper.server;

import bg.web.scraper.scraper.WebScraper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebScraperServer {

    private static final int SERVER_PORT = 6789;
    private static final int MAX_EXECUTOR_THREADS = 10;

    private static WebScraper scraper = new WebScraper();

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        Thread.currentThread().setName("Echo Server Thread");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            System.out.println("Server started and listening for connect requests");

            Socket clientSocket;

            while (true) {

                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, scraper);

                executor.execute(clientHandler);
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }
}
