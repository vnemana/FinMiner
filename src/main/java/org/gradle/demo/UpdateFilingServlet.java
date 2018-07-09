package org.gradle.demo;

import org.gradle.utilities.FilingDetailPage;
import org.gradle.utilities.StoreFilingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "UpdateFilingServlet", urlPatterns = {"updatefiling"}, loadOnStartup = 0)
public class UpdateFilingServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(SearchFundServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.getWriter().print("Hello, Update Filing Page!");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] filingLinks = request.getParameterValues("filing");
        logger.info(filingLinks[0]);
        String output = "";
        for (int ii=0; ii<filingLinks.length; ii++) {
            output += "[" + filingLinks[ii] + "]\n";
            try {
                addFilingData(filingLinks[ii]);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        request.setAttribute("user", output);
        request.getRequestDispatcher("response.jsp").forward(request, response);
    }

    private void addFilingData(String filingLink) throws ParserConfigurationException, SAXException, IOException {
        FilingDetailPage filingDetailPage = new FilingDetailPage(filingLink);
        String rawFilingURL = filingDetailPage.getRawFiling();
        logger.info ("Raw Filing URL: " + rawFilingURL);
        if (rawFilingURL != null) {
            Get13FServlet get13FServlet = new Get13FServlet();
            HashMap<String, HoldingRecord> holdingRecords = get13FServlet.parseDocument(rawFilingURL);

            StoreFilingData storeFilingData = new StoreFilingData();
            storeFilingData.store13FData(holdingRecords, filingDetailPage);
        }
    }
}
