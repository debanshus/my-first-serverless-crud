package com.serverless.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.connection.DynamoDBAdapter;
import com.serverless.model.Employee;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class EmployeeDAO {
    private static final String EMPLOYEE_TABLE_NAME = "employee_table";

    private Logger logger = Logger.getLogger(this.getClass());
    private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    public EmployeeDAO() {
        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(EMPLOYEE_TABLE_NAME))
                .build();
        // get the db adapter
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        // create the mapper with config
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }

    // methods
    public Boolean ifTableExists() {
        return this.client.describeTable(EMPLOYEE_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }

    public List<Employee> list() throws IOException {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<Employee> results = this.mapper.scan(Employee.class, scanExp);
        for (Employee p : results) {
            logger.info("Employees - list(): " + p.toString());
        }
        return results;
    }

    public Employee get(String id) throws IOException {
        Employee employee = null;

        HashMap<String, AttributeValue> av = new HashMap<>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Employee> queryExp = new DynamoDBQueryExpression<Employee>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(av);

        PaginatedQueryList<Employee> result = this.mapper.query(Employee.class, queryExp);
        if (result.size() > 0) {
            employee = result.get(0);
            logger.info("Employees - get(): Employee - " + employee.toString());
        } else {
            logger.info("Employees - get(): Employee - Not Found.");
        }
        return employee;
    }

    public void save(Employee employee) throws IOException {
        logger.info("Employees - save(): " + employee.toString());
        this.mapper.save(employee);
    }

    public Boolean delete(String id) throws IOException {
        Employee employee = null;

        // get employee if exists
        employee = get(id);
        if (employee != null) {
            logger.info("Employees - delete(): " + employee.toString());
            this.mapper.delete(employee);
        } else {
            logger.info("Employees - delete(): employee - does not exist.");
            return false;
        }
        return true;
    }
}
