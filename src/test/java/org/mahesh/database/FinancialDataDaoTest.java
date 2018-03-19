package org.mahesh.database;

import org.junit.Test;

import static org.junit.Assert.*;

public class FinancialDataDaoTest {

    @Test
    public void insertInto13F() {
        FinancialDataDao fData = new FinancialDataDao();
        fData.insertInto13F("Berkshire Hathaway", "GOOGL", "02079K305", "2018-03-05",
                2018, 1, 1079206000, 20742000);
    }
}