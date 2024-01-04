package bg.web.scraper.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int SERVER_PORT = 6789;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // autoflush on
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            Thread.currentThread().setName("Echo client thread " + socket.getLocalPort());

            System.out.println("Connected to the server.");

            while (true) {
                waitReply(reader);

                System.out.print("Enter message: ");

                String message = scanner.nextLine(); // read a line from the console

                if ("quit".equals(message)) {
                    break;
                }

                System.out.println("Sending message <" + message + "> to the server...");

                writer.println(message); // send the message to the server

            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    private static void waitReply(BufferedReader reader) {
        String reply = "\n";
        while (!reply.equals("clear")) {
            System.out.println(reply);
            try {
                reply  = reader.readLine();

                if (reply.equals("quit")) {
                    System.exit(0);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
