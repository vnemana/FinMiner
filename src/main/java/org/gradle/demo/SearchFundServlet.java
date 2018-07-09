package org.gradle.demo;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.gradle.utilities.Search13FResultsPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


//This servlet will search through EDGAR Company filings for the hedge fund or company and return the results of the
// query
@WebServlet(name="SearchFundServlet", urlPatterns = {"searchfund"})
public class SearchFundServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(SearchFundServlet.class);
    private static final String searchUrl = "https://www.sec.gov/cgi-bin/browse-edgar?company=";
    private static final String searchSite = "https://www.sec.gov";
    private static final String search13fParam = "&type=13F-HR&count=100";

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

            HtmlElement body = page.getBody();
            HtmlElement series = body.getOneHtmlElementByAttribute("div", "id", "seriesDiv");
            HtmlTable table = series.getOneHtmlElementByAttribute("table", "summary", "Results");

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
                        logger.info("Search Site: " + search13fSite);
                        Search13FResultsPage s = new Search13FResultsPage(search13fSite);
                        ArrayList<FilingLinkInfo> filing13FLinks = s.get13FFilingLinks();
                        logger.info("Filing Link: " + filing13FLinks.size());
                        if (filing13FLinks.size() > 0) {
                            request.setAttribute("filingLinks", filing13FLinks);
                            request.getRequestDispatcher("OptionalUpdates.jsp").forward(request, response);
                            return;
                        }
//                        if (filing13FLinks != null) {
//                            FilingDetailPage filingDetailPage = new FilingDetailPage(filing13FLinks);
//                            String rawFilingURL = filingDetailPage.getRawFiling();
//                            logger.info ("Raw Filing URL: " + rawFilingURL);
//                            if (rawFilingURL != null) {
//                                Get13FServlet get13FServlet = new Get13FServlet();
//                                HashMap<String, HoldingRecord> holdingRecords = get13FServlet.parseDocument(rawFilingURL);
//                                request.setAttribute("fData", holdingRecords);
//                                request.getRequestDispatcher("13fdata.jsp").forward(request, response);
//
//                                StoreFilingData storeFilingData = new StoreFilingData();
//                                storeFilingData.store13FData(holdingRecords, filingDetailPage);
//                            }
//                        }
                    }
                } catch (ElementNotFoundException e) {
                    System.out.println("Element Not Found Exception");
                } catch (ServletException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
