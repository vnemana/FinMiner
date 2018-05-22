package org.gradle.utilities;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.MalformedURLException;
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
//            List<DomElement> domElements = search13fResultsPage.getByXPath("/html/body/div[4]/div[4]/table/tbody/tr[2]/td[2]/a/@href");
//            DomElement dom = domElements.get(0);
//            System.out.println(dom.getTextContent());

            HtmlTable results13fTable = search13fResultsPage.getBody().getOneHtmlElementByAttribute(
                    "table", "summary", "Results");
            int numRows = results13fTable.getRowCount();
            if (numRows > 1) {
                List<HtmlAnchor> htmlAnchors13f = search13fResultsPage.getAnchors();
                for (int rowNum = 1; rowNum < 2; rowNum++) {
                    HtmlTableRow row13F = results13fTable.getRow(rowNum);
                    HtmlTableCell htmlTableCell = row13F.getCell(2);
                    String accountString = htmlTableCell.asText();
                    String accountStringArray[] = accountString.split(":");
                    String accountArray[] = accountStringArray[1].split(" ");
                    for (HtmlAnchor ha13F : htmlAnchors13f) {
                        if (ha13F.getHrefAttribute().contains(accountArray[1])) {
                            return searchSite + ha13F.getHrefAttribute();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
