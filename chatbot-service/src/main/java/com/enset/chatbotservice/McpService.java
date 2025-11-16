package com.enset.chatbotservice;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class McpService {

    @Value("${mcp.server.url}")
    private String mcpServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> getBeneficiaries() {
        try {
            // Utiliser List.class au lieu de Map.class puisque la réponse est un tableau
            List<Map<String, Object>> beneficiaries = restTemplate.getForObject(mcpServerUrl + "/beneficiaries", List.class);

            System.out.println("Bénéficiaires récupérés: " + beneficiaries);

            if (beneficiaries != null && !beneficiaries.isEmpty()) {
                return beneficiaries;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des bénéficiaires: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public String getVirementsOf(String name) {
        try {
            Map response = restTemplate.getForObject(mcpServerUrl + "/virements?beneficiary=" + name, Map.class);
            if (response != null && response.get("content") != null) {
                return (String) response.get("content");
            }
            return "Aucun virement trouvé pour " + name;
        } catch (Exception e) {
            return "Erreur MCP: " + e.getMessage();
        }
    }
}

