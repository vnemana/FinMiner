package org.gradle.demo;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Get13FServletTest {

    @Test
    public void parseDocument() {
        try {
            Get13FServlet get13FServlet = new Get13FServlet();
            get13FServlet.parseDocument(
                    "https://www.sec.gov/Archives/edgar/data/1067983/000095012318002390/form13fInfoTable.xml");
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}