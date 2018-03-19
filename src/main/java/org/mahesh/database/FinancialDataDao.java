package org.mahesh.database;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.util.HashMap;
import java.util.Map;

public class FinancialDataDao {

    public void insertInto13F(String whale, String stock, String cusip, String filingDate, int year, int quarter,
                              double position, int numShares) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("financial_data");

        final Map<String, Object> infoMap = new HashMap<>();
        infoMap.put ("stock", stock);
        infoMap.put ("cusip", cusip);
        infoMap.put ("filing_date", filingDate);
        infoMap.put ("year", year);
        infoMap.put ("quarter", quarter);
        infoMap.put ("position_value", position);
        infoMap.put ("num_shares", numShares);

        try {
            PutItemOutcome outcome = table.putItem(new Item()
                    .withPrimaryKey("cusip", cusip)
                    .withString("stock", stock)
                    .withString("whale", whale)
                    .withString("filing_date", filingDate)
                    .withNumber("year", year)
                    .withNumber("quarter", quarter)
                    .withNumber("position_value", position)
                    .withNumber("num_shares", numShares));
            System.out.println("PutItem succeeded: " + outcome.getPutItemResult());
        }catch (Exception e) {
            System.err.println("Unable to read table - financial_data");
            System.err.println(e.getMessage());
        }
    }
}
