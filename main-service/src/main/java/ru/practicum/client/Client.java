package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.NewStatisticDto;

@Service
public class Client {
    protected final RestTemplate rest;

    @Autowired
    public Client(@Value("${stats.url}") String serverUrl, RestTemplateBuilder builder) {

        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();

    }

    public ResponseEntity<Object> post(String path, NewStatisticDto body) {
        return makeAndSendRequest(HttpMethod.POST, path, body);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, NewStatisticDto body) {
        HttpEntity<NewStatisticDto> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> response;
        try {
            response = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(response);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}
