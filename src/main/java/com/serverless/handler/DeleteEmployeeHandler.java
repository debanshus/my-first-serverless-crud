package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dao.EmployeeDAO;
import com.serverless.response.ApiGatewayResponse;
import com.serverless.response.Response;
import org.apache.log4j.Logger;

import java.util.Map;

public class DeleteEmployeeHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String employeeId = pathParameters.get("id");
            Boolean success = new EmployeeDAO().delete(employeeId);
            if (success) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(204)
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Employee with id: '" + employeeId + "' not found.")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Error in deleting Employee: " + ex);
            Response responseBody = new Response("Error in deleting Employee: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }
    }
}