import bg.web.scraper.scraper.WebScraper;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        WebScraper wc = new WebScraper();
        try {
            List<String> links = wc.scrapeLinks("https://en.wikipedia.org/wiki/Cat");
            for (String lnk : links) {
                System.out.println(lnk);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //wc.scrapeParagraph("https://en.wikipedia.org/wiki/Cat");
        //wc.scrapeArticle("https://ritholtz.com/2024/01/state-coincident-indicators-slipping/",
        //   "article > *");
    }
}
