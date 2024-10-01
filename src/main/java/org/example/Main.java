package org.example;

import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
        String jsonResponse = "aa";
        ;
        try {
            var fcgiInterface = new FCGIInterface();


            String body = "au";


            logger.warn(body);
            logger.info("Поехали");
            //Utils.intoLogs("Поехали");
            while (fcgiInterface.FCGIaccept() >= 0) {
                if (!(fcgiInterface.request == null)) {
                    Properties requestProperties = fcgiInterface.request.params;
                    if (requestProperties.get("REQUEST_METHOD").equals("POST")) {
                        body = Utils.readRequestBody();
                        //Utils.intoLogs(body);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Request request = objectMapper.readValue(body, Request.class);
                        float x = request.x();
                        int y = request.y();
                        float r = request.r();

                        boolean check = Check.checkCoordinates(x, y, r);
                        Response response = new Response(check, x, y, r);
                        ObjectMapper objectMapper1 = new ObjectMapper();
                        jsonResponse = objectMapper1.writeValueAsString(response);

                        logger.info(jsonResponse);
                    }
                } else {
                    logger.info("Нет реквеста");
                }

                var content = """
                        %s
                        """.formatted(jsonResponse);
                var httpResponse = """
                        HTTP/1.1 200 OK
                        Content-Type: application/json
                        Content-Length: %d
                                            
                        %s
                        """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
                System.out.println(httpResponse);
            }

        } catch (
                Exception e) {
            logger.info(e.getMessage());
        }
    }
}