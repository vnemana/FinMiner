package org.gradle.utilities;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public class Search13FResultsPage {
    private static final String searchSite = "https://www.sec.gov";
    private HtmlPage search13fResultsPage;

    public Search13FResultsPage (String url) {
        try (final WebClient webClient = new WebClient()) {
            search13fResultsPage = webClient.getPage(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public String get13FFilingLink () {
        List<DomNode> domNodes = search13fResultsPage.getByXPath(
                "/html/body/div[4]/div[4]/table/tbody/tr[2]/td[2]/a/@href");
        if (domNodes.size() == 1)
            return searchSite + domNodes.get(0).getTextContent();
        return null;
    }
}
