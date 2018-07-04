package org.gradle.utilities;

import org.junit.Test;

public class FilingDetailPageTest {

    @Test
    public void parseFilingDetailPage() {
        FilingDetailPage filingDetailPage = new FilingDetailPage(
                "https://www.sec.gov/Archives/edgar/data/1067983/000095012318005732/0000950123-18-005732-index.htm");
        filingDetailPage.getRawFiling();
    }


    @Test
    public void getFilingDate() {
        FilingDetailPage filingDetailPage = new FilingDetailPage(
                "https://www.sec.gov/Archives/edgar/data/1067983/000095012318005732/0000950123-18-005732-index.htm");
        System.out.println(filingDetailPage.getFilingDate().getTime());
    }

    @Test
    public void getReportDate() {
        FilingDetailPage filingDetailPage = new FilingDetailPage(
                "https://www.sec.gov/Archives/edgar/data/1067983/000095012318005732/0000950123-18-005732-index.htm");
        System.out.println(filingDetailPage.getReportDate().getTime());
    }

    @Test
    public void getCompanyName() {
        FilingDetailPage filingDetailPage = new FilingDetailPage(
                "https://www.sec.gov/Archives/edgar/data/1067983/000095012318005732/0000950123-18-005732-index.htm");
        System.out.println(filingDetailPage.getCompanyName());
    }
}