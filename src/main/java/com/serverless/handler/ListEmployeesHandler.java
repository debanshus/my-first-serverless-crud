package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dao.EmployeeDAO;
import com.serverless.response.ApiGatewayResponse;
import com.serverless.response.Response;
import org.apache.log4j.Logger;

import java.util.Map;

public class ListEmployeesHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(new EmployeeDAO().list())
                    .build();
        } catch (Exception ex) {
            logger.error("Error in listing employees: " + ex);
            Response responseBody = new Response("Error in listing employees: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }
    }
}