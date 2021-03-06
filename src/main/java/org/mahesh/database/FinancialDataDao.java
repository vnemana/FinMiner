package org.mahesh.database;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "financial_data")
public class FinancialDataDao {

    private String id;
    private String whale;
    private String cusip;
    private long filingDate;
    private long reportDate;
    private double position;
    private long numShares;
    private String stock;

    @DynamoDBHashKey (attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    @DynamoDBAttribute (attributeName = "whale")
    public String getWhale() {return whale;}
    public void setWhale(String whale) {this.whale = whale;}

    @DynamoDBAttribute (attributeName = "cusip")
    public String getCusip() {return cusip;}
    public void setCusip (String cusip) {this.cusip = cusip;}

    @DynamoDBAttribute (attributeName = "stock")
    public String getStock() {return stock;}
    public void setStock (String stock) {this.stock = stock;}

    @DynamoDBAttribute (attributeName = "filing_date")
    public long getFilingDate() {return filingDate;}
    public void setFilingDate(long filingDate) {this.filingDate = filingDate;}

    @DynamoDBAttribute (attributeName = "report_date")
    public long getReportDate() {return reportDate;}
    public void setReportDate(long reportDate) {this.reportDate = reportDate;}

    @DynamoDBAttribute (attributeName = "position")
    public double getPosition() {return position;}
    public void setPosition(double position) {this.position = position;}

    @DynamoDBAttribute (attributeName = "num_shares")
    public long getNumShares() {return numShares;}
    public void setNumShares(long numShares) {this.numShares = numShares;}

    public void insertInto13F() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();
        DynamoDBMapper dbMapper = new DynamoDBMapper(client);
        dbMapper.save(this);
    }
}
