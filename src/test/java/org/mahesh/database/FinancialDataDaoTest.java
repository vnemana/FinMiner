package org.mahesh.database;

import org.junit.Test;

public class FinancialDataDaoTest {

    @Test
    public void insertInto13F() {
        FinancialDataDao fData = new FinancialDataDao();
        fData.setWhale("Berkshire Hathaway");
        fData.setCusip("02079K305");
        fData.setStock("GOOGL");
        fData.setFilingDate(20180305);
        fData.setReportDate(20171231);
        fData.setPosition(1079206000);
        fData.setNumShares(20742000);
        fData.insertInto13F();
    }
}