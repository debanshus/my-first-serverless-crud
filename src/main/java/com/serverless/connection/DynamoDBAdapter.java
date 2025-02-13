package com.serverless.connection;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class DynamoDBAdapter {

    private static DynamoDBAdapter adapter = null;
    private final AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    private DynamoDBAdapter() {
        // create the client
        this.client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    }

    public static DynamoDBAdapter getInstance() {
        if (adapter == null) adapter = new DynamoDBAdapter();
        return adapter;
    }

    public AmazonDynamoDB getDbClient() {
        return this.client;
    }

    public DynamoDBMapper createDbMapper(DynamoDBMapperConfig mapperConfig) {
        // create the mapper with the mapper config
        if (this.client != null) mapper = new DynamoDBMapper(this.client, mapperConfig);
        return this.mapper;
    }

}
