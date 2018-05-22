package org.gradle.demo;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.gradle.utilities.FilingDetailPage;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
                        final HtmlPage search13fResultsPage = webClient.getPage(search13fSite);
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
                                        String filing13FLink = searchSite + ha13F.getHrefAttribute();
                                        FilingDetailPage filingDetailPage = new FilingDetailPage(filing13FLink);
                                        String rawFilingURL = filingDetailPage.getRawFiling();
                                        rawFilingURL = searchSite + rawFilingURL;
                                        if (rawFilingURL != null) {
                                            Get13FServlet get13FServlet = new Get13FServlet();
                                            HashMap<String, HoldingRecord> holdingRecords = get13FServlet.parseDocument(rawFilingURL);
                                            request.setAttribute("fData", holdingRecords);
                                            request.getRequestDispatcher("13fdata.jsp").forward(request, response);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (ElementNotFoundException e) {
                    System.out.println("Element Not Found Exception");
                    //e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (ServletException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
