package com.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.connection.DynamoDBAdapter;
import com.serverless.model.Employee;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class EmployeeDAO {
    private Logger logger = Logger.getLogger(this.getClass());
    private final DynamoDBMapper mapper;

    public EmployeeDAO() {
        // create the mapper with config
        mapper = DynamoDBAdapter.getInstance().createDbMapper(DynamoDBMapperConfig.builder().build());
    }

    public List<Employee> list() {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<Employee> results = this.mapper.scan(Employee.class, scanExp);
        for (Employee p : results) {
            logger.info("Employees - list(): " + p.toString());
        }
        return results;
    }

    public Employee get(String id) {
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

    public void save(Employee employee) {
        logger.info("Employees - save(): " + employee.toString());
        this.mapper.save(employee);
    }

    public void update(Employee employee) {
        logger.info("Employees - update(): " + employee.toString());
        this.mapper.save(employee, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.PUT));
    }

    public Boolean delete(String id) {
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
