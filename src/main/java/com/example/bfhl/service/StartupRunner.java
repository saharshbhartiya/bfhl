package com.example.bfhl.service;

import com.example.bfhl.model.WebhookRequest;
import com.example.bfhl.model.WebhookResponse;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class StartupRunner {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() {
        RestTemplate restTemplate = new RestTemplate();

        WebhookRequest request = new WebhookRequest("John Doe", "REG12347", "john@example.com");

        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
                "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA",
                request,
                WebhookResponse.class
        );

        WebhookResponse body = response.getBody();
        assert body != null;
        System.out.println(body.getAccessToken());
        String webhook = body.getWebhook();
        String accessToken = body.getAccessToken();

        String finalQuery = """
            SELECT 
                p.AMOUNT AS SALARY,
                CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
                TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
                d.DEPARTMENT_NAME
            FROM PAYMENTS p
            JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
            JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            WHERE DAY(p.PAYMENT_TIME) != 1
            ORDER BY p.AMOUNT DESC
            LIMIT 1;
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, String> payload = Map.of("finalQuery", finalQuery);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> webhookResponse = restTemplate.postForEntity(webhook, entity, String.class);
        System.out.println("Submission Status: " + webhookResponse.getStatusCode());
    }
}
