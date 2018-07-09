package org.gradle.utilities;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import org.gradle.demo.FilingLinkInfo;

import java.io.IOException;
import java.util.ArrayList;
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

    public ArrayList<FilingLinkInfo> get13FFilingLinks() {
        ArrayList<FilingLinkInfo>links = new ArrayList<>();
        List<DomNode> domNodes = search13fResultsPage.getByXPath("/html/body/div[4]/div[4]/table");
        if (domNodes.size() == 1) {
            final HtmlTable table = (HtmlTable) domNodes.get(0);
            List<HtmlTableRow> rows = table.getRows();
            for (int ii = 0; ii < rows.size() - 2; ii++) {

                List<DomNode> site = search13fResultsPage.getByXPath(
                        "/html/body/div[4]/div[4]/table/tbody/tr[" + (ii + 2) + "]/td[2]/a/@href");
                List<DomNode> docType = search13fResultsPage.getByXPath(
                        "/html/body/div[4]/div[4]/table/tbody/tr[" + (ii + 2) + "]/td[1]");
                List<DomNode> fDate = search13fResultsPage.getByXPath(
                        "/html/body/div[4]/div[4]/table/tbody/tr[" + (ii + 2) + "]/td[4]");

                if (docType.get(0).getTextContent().equals("13F-HR")) {
                    FilingLinkInfo fInfo = new FilingLinkInfo();
                    fInfo.setLink(searchSite + site.get(0).getTextContent());
                    fInfo.setFilingDate(fDate.get(0).getTextContent());
                    links.add(fInfo);
                }
            }
        }
        return links;
    }
}
