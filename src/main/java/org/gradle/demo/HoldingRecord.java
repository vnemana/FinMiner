package org.gradle.demo;

public class HoldingRecord implements Comparable{
    String issuerName;
    String cusip;

    public String getIssuerName() {
        return issuerName;
    }

    public String getCusip() {
        return cusip;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    int numberOfShares;

    HoldingRecord(String issuerName, String cusip, int numberOfShares) {
        this.issuerName = issuerName;
        this.cusip = cusip;
        this.numberOfShares = numberOfShares;
    }


    @Override
    public int compareTo(Object o) {
        if (this.cusip.equals(((HoldingRecord)o).cusip))
            return 0;
        else return 1;
    }
}
