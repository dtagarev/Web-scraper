package bg.web.scraper.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebScraper {

    private final List<String> fileNames;

    public WebScraper() {
        fileNames = Collections.synchronizedList(new ArrayList<String>());
    }

    public List<String> scrapeLinks(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements linkElements = document.select("a[href]");

        List<String> links = new ArrayList<>();

        for (Element linkElement : linkElements) {
            String href = linkElement.attr("href");

            if (href.startsWith("http")) {
                links.add(href);
            }
        }

        return links;
    }

    public void scrapePictures(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String fetchWikipediaArticle(String url) throws IOException {
        // Connect to the Wikipedia page and fetch the HTML content
        Document document = Jsoup.connect(url).get();

        // Extract the article content
        Element contentElement = document.getElementById("mw-content-text");
        if (contentElement != null) {
            Elements paragraphs = contentElement.select("h1,h2,h3,p");
            StringBuilder contentBuilder = new StringBuilder();

            for (Element paragraph : paragraphs) {
                contentBuilder.append(paragraph.text()).append("\n\n");
            }

            return contentBuilder.toString();
        } else {
            return "Unable to locate the article content.";
        }
    }

    public void saveArticleToFile(String fileName, String article) throws FileAlreadyExistsException {
        if (fileNames.contains(fileName)) {
            throw new FileAlreadyExistsException("The file " + fileName + " already exists");
        }

        fileNames.add(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(article);
            System.out.println("Content saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHyperlinksToFile(String fileName, List<String> links) throws FileAlreadyExistsException {
        if (fileNames.contains(fileName)) {
            throw new FileAlreadyExistsException("The file " + fileName + " already exists");
        }

        fileNames.add(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String link : links) {
                writer.write(link);
            }
            System.out.println("Content saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
