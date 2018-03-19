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
    protected static final String searchUrl = "https://www.sec.gov/cgi-bin/browse-edgar?company=";
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.getWriter().print("Hello, SearchFund!");
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String fundString = request.getParameter("searchstring");
        if (fundString == null) fundString = "Berkshire Hathaway";
        String urlString = searchUrl + fundString;

        System.out.println("URL: < " + urlString + ">");
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(urlString);

            List<DomElement> nodes = page.getElementsByName("contentDiv");

            System.out.println("Page: <" + page.getBody().getNodeName() + ">");
            HtmlElement body = page.getBody();
            HtmlElement series = body.getOneHtmlElementByAttribute("div", "id", "seriesDiv");
            HtmlTable table = series.getOneHtmlElementByAttribute("table", "summary", "Results");

            System.out.println("Table Row Count: " + table.getRowCount());
            //System.out.println(page.getAnchors().toString());
            int rowCount = 0;
            for (HtmlTableRow row : table.getRows()) {
                if (rowCount == 0) {
                    rowCount++;
                    continue;
                }
                try {
                    HtmlTableCell cell = row.getCell(0);
                    String anchorText = cell.asText();
                    HtmlAnchor ha = page.getAnchorByText(anchorText);
                    if (ha != null) {
                        System.out.println(ha.getHrefAttribute());
                    }
                } catch (ElementNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("----");
            }
        }
    }
}
