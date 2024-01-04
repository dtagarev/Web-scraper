package bg.web.scraper.server;

import bg.web.scraper.scraper.WebScraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private final WebScraper scraper;

    public ClientRequestHandler(Socket socket, WebScraper scraper) {
        this.socket = socket;
        this.scraper = scraper;
    }

    private void printMenu(PrintWriter out) {
        out.println("======= Web Scraper Main Menu =======");
        out.println("1. Scrape Wikipedia article");
        out.println("2. Scrape all hyperlinks from a site");
        out.println("3. Exit");
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (true) {
                printMenu(out);
                String choice = waitInput(out, in);
                switch (choice) {
                    case "1":
                        // Scrape Wikipedia article
                        try {
                            scrapeWikipediaArticle(out, in);
                        } catch (RuntimeException e) {
                            out.println("Invalid Url, please try again");
                            continue;
                        }
                        break;

                    case "2":
                        // Scrape all hyperlinks from a site
                        try {
                            scrapeLinks(out, in);
                        } catch (RuntimeException e) {
                            out.println("Invalid Url, please try again");
                            continue;
                        }
                        break;

                    case "3":
                        // Option 3: Exit the program
                        out.println("Exiting the program");
                        out.println("quit");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //System.exit(0);
                        return;
                    default:
                        out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private void scrapeLinks(PrintWriter out, BufferedReader in) {
        List<String> links = null;
        String url;
        try {
            out.println("Enter the site url");
            out.println("clear");
            url = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            links = scraper.scrapeLinks(url);
        } catch (IOException e) {
            out.println("Invalid Url, please try again");
            e.printStackTrace();
            return;
        }

        for (String link : links) {
            out.println(link);
        }

        out.println("======= Hyperlink Scraper Menu =======");
        out.println("1. Save hyperlinks to file");
        out.println("2. Back");

        while (true) {
            String choice = waitInput(out, in);

            switch (choice) {
                case "1":
                    while (true) {
                        out.println("Enter file name");
                        out.println("clear");
                        String fileName = null;
                        try {
                            fileName = in.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            scraper.saveHyperlinksToFile(fileName, links);
                            out.println("Hyperlinks saved to file");
                            break;
                        } catch (FileAlreadyExistsException e) {
                            out.println(e);
                        }
                    }
                    return;
                case "2":
                    return;
                default:
                    out.println("Invalid choice. Please enter a valid option.");
                    break;

            }
        }
    }

    private void scrapeWikipediaArticle(PrintWriter out, BufferedReader in) {
        String article = null;
        String url;
        try {
            out.println("Enter the article url");
            out.println("clear");
            url = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            article = scraper.fetchWikipediaArticle(url);
        } catch (IOException e) {
            out.println("Invalid Url, please try again");
            e.printStackTrace();
            return;
        }

        out.println(article);

        out.println("======= Wikipedia Scraper Menu =======");
        out.println("1. Save article to file");
        out.println("2. Back");

        while (true) {
            String choice = waitInput(out, in);

            switch (choice) {
                case "1":
                    while (true) {
                        out.println("Enter file name");
                        out.println("clear");
                        String fileName = null;
                        try {
                            fileName = in.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            scraper.saveArticleToFile(fileName, article);
                            out.println("Article saved to file");
                            break;
                        } catch (FileAlreadyExistsException e) {
                            out.println(e);
                        }
                    }
                    return;
                case "2":
                    return;
                default:
                    out.println("Invalid choice. Please enter a valid option.");
                    break;

            }
        }
    }

    private String waitInput(PrintWriter out, BufferedReader in) {

        String strChoice = null;
        try {
            out.println("clear");
            strChoice = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (strChoice.equals("terminate")) {
            out.println("Terminating server");
            out.println("quit");
            terminate();
            return "-1";
        }

        return strChoice;
    }

    private void terminate() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
