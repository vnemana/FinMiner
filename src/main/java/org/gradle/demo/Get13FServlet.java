package org.gradle.demo;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.Iterator;

@WebServlet(name = "Get13FServlet", urlPatterns = {"get13f"}, loadOnStartup = 1)
public class Get13FServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().print("Hello, 13F!");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String urlString = request.getParameter("13furl");
        if (urlString == null) urlString = "13F";
        try {
            HashMap<String, HoldingRecord> holdingRecords = parseDocument(urlString);
            request.setAttribute("fData", holdingRecords);
            request.getRequestDispatcher("13fdata.jsp").forward(request, response);
        }
        catch(ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    protected HashMap<String, HoldingRecord> parseDocument(String urlString)
            throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.parse(urlConnection.getInputStream());

        System.out.println("Root: " + document.getDocumentElement().getNodeName());

        NodeList nodeList = document.getElementsByTagName("infoTable");
        System.out.println("Number of Nodes: " + nodeList.getLength());
        HashMap<String, HoldingRecord> holdingRecords = new HashMap<>();

        for (int nodeCount=0; nodeCount<nodeList.getLength(); nodeCount++) {
            Node node = nodeList.item(nodeCount);
            Element element = (Element) node;
            String issuerName = element.getElementsByTagName("nameOfIssuer").item(0).getTextContent();
            String cusip = element.getElementsByTagName("cusip").item(0).getTextContent();
            int numberOfShares = Integer.valueOf(element.getElementsByTagName("sshPrnamt").item(0).getTextContent());

            HoldingRecord hr = new HoldingRecord(issuerName, cusip, numberOfShares);
            HoldingRecord existingHr = holdingRecords.get(cusip);
            if (existingHr != null) {
                existingHr.numberOfShares += numberOfShares;
            }
            else {
                holdingRecords.put(cusip, hr);
            }
        }

        Iterator iterator = holdingRecords.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            HoldingRecord hr = (HoldingRecord) pair.getValue();
            //System.out.println(hr.cusip + " "  + hr.issuerName + " " + hr.numberOfShares);
        }
        return holdingRecords;
    }
}
