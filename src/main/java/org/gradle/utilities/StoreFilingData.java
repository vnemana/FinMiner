package org.gradle.utilities;

import org.gradle.demo.HoldingRecord;
import org.mahesh.database.FinancialDataDao;

import java.util.HashMap;
import java.util.Iterator;

public class StoreFilingData {
    public StoreFilingData() {}

    public void store13FData(HashMap<String, HoldingRecord> holdingRecords, FilingDetailPage filingDetailPage) {
        Iterator it = holdingRecords.entrySet().iterator();
        String whale = filingDetailPage.getCompanyName();
        long filingDate = filingDetailPage.getFilingDate().getTime();
        long reportDate = filingDetailPage.getReportDate().getTime();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            FinancialDataDao fData = new FinancialDataDao();

            HoldingRecord holdingRecord = (HoldingRecord) pair.getValue();
            fData.setWhale(whale);
            fData.setFilingDate(filingDate);
            fData.setReportDate(reportDate);
            fData.setCusip(holdingRecord.getCusip());
            fData.setStock(holdingRecord.getIssuerName());
            fData.setPosition(holdingRecord.getPosition());
            fData.setNumShares(holdingRecord.getNumberOfShares());
            //fData.insertInto13F();
        }
    }
}
