package org.gradle.utilities;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class FilingDetailPage {
    private String filingDetailURL;
    private static final String searchSite = "https://www.sec.gov";

    public FilingDetailPage(String url) {
        filingDetailURL = url;
    }

    public String getRawFiling() {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage filing13FPage = webClient.getPage(filingDetailURL);
            HtmlTable resultsFiling13FTable = filing13FPage.getBody().getOneHtmlElementByAttribute("table",
                    "summary", "Document Format Files");
            int numFormatFileRows = resultsFiling13FTable.getRowCount();
            //for each row
            //get "Document" cell text.
            //If it says "form13fInfoTable.html", get the link.
            String source = "/html/body/div[4]/div[2]/div[1]/table/tbody/tr[:?:]/td[3]/a/@href";
            String[] sourceArr = source.split(":");
            for (int ii = 0; ii < numFormatFileRows; ii++) {
                HtmlTableRow ii_row = resultsFiling13FTable.getRow(ii);
                HtmlTableCell ii_rowCell = ii_row.getCell(2);
                String docString = ii_rowCell.asText();
                if (docString.equals("form13fInfoTable.xml")) {
                    String sourceData = sourceArr[0] + (ii+1) + sourceArr[2];
                    List<DomNode> sourceNodes = filing13FPage.getByXPath(sourceData);
                    if (sourceNodes.size() > 0)
                        return searchSite + sourceNodes.get(0).getTextContent();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}