package org.gradle.demo;

public class HoldingRecord implements Comparable{
    private final String issuerName;
    private final String cusip;

    public String getIssuerName() {
        return issuerName;
    }

    public String getCusip() {
        return cusip;
    }

    public long getNumberOfShares() {
        return numberOfShares;
    }

    public long getPosition() { return position; }

    long numberOfShares;
    long position;

    HoldingRecord(String issuerName, String cusip, long numberOfShares, long position) {
        this.issuerName = issuerName;
        this.cusip = cusip;
        this.numberOfShares = numberOfShares;
        this.position = position;
    }


    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Object o) {
        if (this.cusip.equals(((HoldingRecord)o).cusip))
            return 0;
        else return 1;
    }
}
