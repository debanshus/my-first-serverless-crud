package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dao.EmployeeDAO;
import com.serverless.model.Employee;
import com.serverless.response.ApiGatewayResponse;
import com.serverless.response.Response;
import org.apache.log4j.Logger;

import java.util.Map;

public class CreateEmployeeHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            Employee employee = new Employee();
            employee.setName(body.get("name").asText());

            EmployeeDAO dao = new EmployeeDAO();
            dao.save(employee);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(employee)
                    .build();
        } catch (Exception ex) {
            logger.error("Error in saving employee: " + ex);
            Response responseBody = new Response("Error in saving employee: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }
    }
}