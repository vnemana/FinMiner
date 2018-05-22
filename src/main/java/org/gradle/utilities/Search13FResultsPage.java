package org.gradle.utilities;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;

public class Search13FResultsPage {
    private String searchURL;
    private static final String searchSite = "https://www.sec.gov";

    public Search13FResultsPage (String url) {
        searchURL = url;
    }

    public String get13FFilingLink () {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage search13fResultsPage = webClient.getPage(searchURL);
            List<DomNode> domNodes = search13fResultsPage.getByXPath(
                    "/html/body/div[4]/div[4]/table/tbody/tr[2]/td[2]/a/@href");
            if (domNodes.size() == 1) {
                return searchSite + domNodes.get(0).getTextContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
