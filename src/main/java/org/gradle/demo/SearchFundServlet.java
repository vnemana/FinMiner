package org.gradle.demo;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;


//This servlet will search through EDGAR Company filings for the hedge fund or company and return the results of the
// query
@WebServlet(name="SearchFundServlet", urlPatterns = {"searchfund"})
public class SearchFundServlet extends HttpServlet {
    private static final String searchUrl = "https://www.sec.gov/cgi-bin/browse-edgar?company=";
    private static final String searchSite = "https://www.sec.gov";
    private static final String search13fParam = "&type=13F-HR";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.getWriter().print("Hello, SearchFund!");
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String fundString = request.getParameter("searchstring");
        if (fundString == null) fundString = "Berkshire Hathaway";
        String urlString = searchUrl + fundString;

        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(urlString);

            List<DomElement> nodes = page.getElementsByName("contentDiv");

            HtmlElement body = page.getBody();
            HtmlElement series = body.getOneHtmlElementByAttribute("div", "id", "seriesDiv");
            HtmlTable table = series.getOneHtmlElementByAttribute("table", "summary", "Results");

            //System.out.println(page.getAnchors().toString());
            boolean firstRow = true;
            for (HtmlTableRow row : table.getRows()) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                try {
                    HtmlTableCell cell = row.getCell(0);
                    String anchorText = cell.asText();
                    HtmlAnchor ha = page.getAnchorByText(anchorText);
                    if (ha != null) {
                        String search13fSite = searchSite + ha.getHrefAttribute() + search13fParam;
                        //System.out.println("Search13fSite: " + search13fSite);
                        final HtmlPage search13fResultsPage = webClient.getPage(search13fSite);
                        HtmlTable results13fTable = search13fResultsPage.getBody().getOneHtmlElementByAttribute(
                                "table", "summary", "Results");
                        int numRows = results13fTable.getRowCount();
                        if (numRows <= 1)
                            System.out.println("No Results");
                        else {
                            System.out.println("Num Results: " + (numRows-1));
                            List<HtmlAnchor> htmlAnchors13f = search13fResultsPage.getAnchors();
                            for (int rowNum = 1; rowNum < 2; rowNum++) {
                                HtmlTableRow row13F = results13fTable.getRow(rowNum);
                                HtmlTableCell htmlTableCell = row13F.getCell(2);
                                String accountString = htmlTableCell.asText();
                                String accountStringArray[] = accountString.split(":");
                                String accountArray[] = accountStringArray[1].split(" ");
                                for (HtmlAnchor ha13F : htmlAnchors13f) {
                                    if (ha13F.getHrefAttribute().contains(accountArray[1])) {
                                        String filing13FLink = searchSite + ha13F.getHrefAttribute();
                                        final HtmlPage filing13FPage = webClient.getPage(filing13FLink);

                                    }
                                }
//                                HtmlAnchor htmlAnchorFiling13F = search13fResultsPage.getAnchorByName("Documents");
//                                if (htmlAnchorFiling13F != null) {
//                                    String filing13FLink = searchSite + htmlAnchorFiling13F.getHrefAttribute();
//                                    System.out.println(filing13FLink);
//                                }
                            }
                        }
                    }
                } catch (ElementNotFoundException e) {
                    System.out.println("Element Not Found Exception");
                    //e.printStackTrace();
                }
            }
        }
    }
}
