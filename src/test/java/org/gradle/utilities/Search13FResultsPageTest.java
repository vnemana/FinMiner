package org.gradle.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

public class Search13FResultsPageTest {

    @Test
    public void get13FFilingLink() {
        Search13FResultsPage s = new Search13FResultsPage(
                "https://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=0001067983&owner=include&count=40" +
                        "&hidefilings=0&type=13F-HR");
        System.out.println(s.get13FFilingLink());
    }
}