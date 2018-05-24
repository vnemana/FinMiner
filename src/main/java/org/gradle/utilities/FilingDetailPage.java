package org.gradle.utilities;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FilingDetailPage {
    private String companyName;
    private HtmlPage filing13FPage;
    private static final String searchSite = "https://www.sec.gov";

    public FilingDetailPage(String url) {
        try (final WebClient webClient = new WebClient()) {
            filing13FPage = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRawFiling() {
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
        return null;
    }

    Date getFilingDate() {
        try {
            String source = "/html/body/div[4]/div[1]/div[2]/div[1]/div[2]";
            List<DomNode> domNodes = filing13FPage.getByXPath(source);
            if(domNodes.size() > 0) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.parse(domNodes.get(0).getTextContent());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    Date getReportDate() {
        try {
            String source = "/html/body/div[4]/div[1]/div[2]/div[2]/div[2]";
            List<DomNode> domNodes = filing13FPage.getByXPath(source);
            if(domNodes.size() > 0) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.parse(domNodes.get(0).getTextContent());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    String getCompanyName() {
        if (companyName != null && ! companyName.isEmpty())
            return companyName;

        try (final WebClient webClient = new WebClient()){
            String source = "/html/body/div[4]/div[2]/div[1]/table/tbody/tr[2]/td[3]/a/@href";
            List<DomNode> domNodes = filing13FPage.getByXPath(source);
            if (domNodes.size() > 0) {
                final HtmlPage primaryDoc = webClient.getPage(searchSite+domNodes.get(0).getTextContent());
                String companySource = "/html/body/table[4]/tbody/tr[2]/td[2]";
                List<DomNode> domNodesList = primaryDoc.getByXPath(companySource);

                if (domNodesList.size() > 0) {
                    companyName = domNodesList.get(0).getTextContent();
                    return companyName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}