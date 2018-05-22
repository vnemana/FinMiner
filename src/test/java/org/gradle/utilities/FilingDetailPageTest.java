package org.gradle.utilities;

import org.junit.Test;

public class FilingDetailPageTest {

    @Test
    public void parseFilingDetailPage() {
        FilingDetailPage filingDetailPage = new FilingDetailPage(
                "https://www.sec.gov/Archives/edgar/data/1067983/000095012318005732/0000950123-18-005732-index.htm");
    }

}