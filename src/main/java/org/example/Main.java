package org.example;

import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        final Logger logger = LogManager.getLogger(Main.class);
        String jsonResponse = "";
        try {
            var fcgiInterface = new FCGIInterface();


            String body = "au";
            logger.info("Поехали");
            while (fcgiInterface.FCGIaccept() >= 0) {
                if (!(fcgiInterface.request == null)) {
                    Properties requestProperties = fcgiInterface.request.params;
                    if (requestProperties.get("REQUEST_METHOD").equals("POST")) {
                        body = Utils.readRequestBody();
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(body);
                        if (jsonNode.get("x") == null || jsonNode.get("y") == null || jsonNode.get("r") == null){
                            System.out.println(errorResponse("Not enough data"));
                            logger.info("Not enough data");
                            continue;
                        }
                        if (isEmpty(jsonNode)){
                            System.out.println(errorResponse("Empty request"));
                            logger.info("Empty request");
                            continue;
                        }

                        logger.info(body);
                        try{
                            Request request = objectMapper.readValue(body, Request.class);
                            float x = request.x();
                            int y = request.y();
                            float r = request.r();
                            boolean values_ok = Check.checkValues(x, y, r);
                            if (!values_ok){
                                System.out.println(errorResponse("Invalid data (watch constraints)"));
                                logger.info("Invalid data (watch constraints)");
                                continue;
                            }
                            boolean check = Check.checkCoordinates(x, y, r);
                            Response response = new Response(check, x, y, r);
                            ObjectMapper objectMapper1 = new ObjectMapper();
                            jsonResponse = objectMapper1.writeValueAsString(response);

                            logger.info(jsonResponse);
                        }
                        catch (Exception e){
                            System.out.println(errorResponse("Invalid data"));
                            logger.info("Invalid data");
                            continue;
                        }
                    }
                    else{
                        System.out.println(errorResponse("Only POST requests"));
                        logger.info("Wrong request");
                    }
                } else {
                    System.out.println(errorResponse("No request provided"));
                    logger.info("No request");
                }


                System.out.println(successResponse(jsonResponse));
            }

        } catch (
                Exception e) {
            logger.info(e.getMessage());
        }
    }
    private static String successResponse(String jsonResponse){
        var content = """
                        %s
                        """.formatted(jsonResponse);
        var httpResponse = """
                        HTTP/1.1 200 OK
                        Content-Type: application/json
                        Content-Length: %d
                                            
                        %s
                        """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
        return httpResponse;
    }

    private static String errorResponse(String errorResponse){
        String content = """
                {"error":"%s"}
                """.formatted(errorResponse);
        return """
                HTTP/1.1 400 Bad Request
                Content-Type: application/json charset=utf-8
                Content-Length: %d
                
                
                %s
                """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
    }

    public static boolean isEmpty(final JsonNode elem) {
        return elem == null || elem.isNull() || elem.isEmpty();
    }
}