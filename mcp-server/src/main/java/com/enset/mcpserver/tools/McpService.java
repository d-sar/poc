package com.enset.mcpserver.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class McpService {

    @Value("${beneficiaire.service.url}")
    private String beneficiaireUrl;

    @Value("${virement.service.url}")
    private String virementUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // Récupérer tous les bénéficiaires
    public List<Map<String, Object>> getBeneficiaries() {
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                beneficiaireUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return response.getBody();
    }

    // Récupérer les virements d'un bénéficiaire
    public List<Map<String, Object>> getVirements(String beneficiaryName) {
        String url = virementUrl + "?beneficiary=" + beneficiaryName;
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return response.getBody();
    }
}