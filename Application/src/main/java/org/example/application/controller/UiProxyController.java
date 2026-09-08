package org.example.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

// Controller that redirects any /ui request to the frontend url.
@RestController
public class UiProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/ui/**")
    public ResponseEntity<byte[]> proxyUi(HttpServletRequest request) throws IOException {
        String path = request.getRequestURI().replaceFirst("^/ui", "");
        String query = request.getQueryString();
        String targetUrl = "http://localhost:1962" + path + (query != null ? "?" + query : "");

        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(h ->
                headers.add(h, request.getHeader(h)));
        headers.remove(HttpHeaders.HOST);

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        byte[] body = request.getInputStream().readAllBytes();

        ResponseEntity<byte[]> response = restTemplate.exchange(
                targetUrl, method, new HttpEntity<>(body, headers), byte[].class);

        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(response.getBody());
    }
}
