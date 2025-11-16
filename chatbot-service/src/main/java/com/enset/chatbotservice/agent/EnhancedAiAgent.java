package com.enset.chatbotservice.agent;

import com.enset.chatbotservice.McpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class EnhancedAiAgent {

    @Value("${ollama.api.key}")
    private String apiKey;

    @Value("${ollama.api.url}")
    private String apiUrl;

    @Value("${ollama.api.model}")
    private String model;

    private final McpService mcpService;

    public EnhancedAiAgent(McpService mcpService) {
        this.mcpService = mcpService;
    }

    public String chat(String message) {
        message = message.toLowerCase();

        // Récupérer toutes les données nécessaires
        List<Map<String, Object>> beneficiaries = mcpService.getBeneficiaries();

        // Analyse du message
        if (message.contains("lister")) {
            return formatBeneficiaries(beneficiaries);
        }

        if (message.contains("rib") || message.contains("rip")) {
            String name = extractName(message);
            return findRib(beneficiaries, name);
        }



        // Si pas reconnu, laisser Ollama Cloud répondre
        return callOllamaCloud(message);
    }



    private String findRib(List<Map<String, Object>> beneficiaries, String name) {
        for (Map<String, Object> b : beneficiaries) {
            if (((String)b.get("nom")).equalsIgnoreCase(name)) {
                return "Le RIB de " + b.get("nom") + " est : " + b.get("rib");
            }
        }
        return "Aucun bénéficiaire trouvé avec ce nom.";
    }




    private String extractName(String message) {
        // Exemple simple pour extraire le nom après "virement de"
        return message.replace("virement de", "").trim();
    }

    private String formatBeneficiaries(List<Map<String, Object>> beneficiaries) {
        if (beneficiaries.isEmpty()) return "Aucun bénéficiaire trouvé.";
        StringBuilder sb = new StringBuilder("Liste des bénéficiaires:\n");
        for (Map<String, Object> b : beneficiaries) {
            sb.append("Nom: ").append(b.get("nom"))
                    .append(", Prénom: ").append(b.get("prenom"))
                    .append(", RIB: ").append(b.get("rib"))
                    .append(", Type: ").append(b.get("type"))
                    .append("\n");
        }
        return sb.toString();
    }


    private String formatVirements(Object response) {
        if (response instanceof List<?>) {
            StringBuilder sb = new StringBuilder("Virements:\n");
            for (Object obj : (List<?>) response) {
                sb.append(obj.toString()).append("\n");
            }
            return sb.toString();
        }
        return "Aucun virement trouvé pour cet utilisateur.";
    }

    private String callOllamaCloud(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("stream", false);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Vous êtes un assistant bancaire qui répond uniquement à des questions générales.");
            messages.add(systemMessage);

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.add(userMessage);

            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            Map<?, ?> response = restTemplate.postForObject(apiUrl, entity, Map.class);

            if (response != null && response.get("message") != null) {
                Map<?, ?> messageObj = (Map<?, ?>) response.get("message");
                return (String) messageObj.get("content");
            }
            return "Je n'ai pas pu obtenir de réponse.";
        } catch (Exception e) {
            return "Erreur de communication avec Ollama: " + e.getMessage();
        }
    }
}
