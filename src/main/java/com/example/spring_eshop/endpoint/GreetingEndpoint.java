package com.example.spring_eshop.endpoint;

import com.example.spring_eshop.config.WebServiceConfig;
import com.example.spring_eshop.service.GreetingService;
import com.example.spring_eshop.ws.greeting.GetGreetingRequest;
import com.example.spring_eshop.ws.greeting.GetGreetingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;

@Endpoint
public class GreetingEndpoint {

    public static final String NAMESPACE_GREETING = "http://example.com/spring_eshop/ws/greeting";
    private final GreetingService greetingService;

    @Autowired
    public GreetingEndpoint(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // как RequestMapping
    @PayloadRoot(namespace = NAMESPACE_GREETING, localPart = "getGreetingRequest")
    @ResponsePayload // полезная нагрузка
    public GetGreetingResponse getGreeting(@RequestPayload GetGreetingRequest request) throws DatatypeConfigurationException {
        GetGreetingResponse response = new GetGreetingResponse();
        response.setGreeting(greetingService.generateGreeting(request.getName()));
        return response;
    }
}
