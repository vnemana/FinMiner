package org.mahesh.database;

import org.junit.Test;

import static org.junit.Assert.*;

public class FinancialDataDaoTest {

    @Test
    public void insertInto13F() {
        FinancialDataDao fData = new FinancialDataDao();
        fData.setWhale("Berkshire Hathaway");
        fData.setCusip("02079K305");
        fData.setStock("GOOGL");
        fData.setFilingDate("2018-03-05");
        fData.setReportDate("2017-12-31");
        fData.setPosition(1079206000);
        fData.setNumShares(20742000);
        fData.insertInto13F();
    }
}