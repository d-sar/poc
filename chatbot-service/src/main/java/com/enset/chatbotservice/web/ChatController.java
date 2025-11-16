package com.enset.chatbotservice.web;

import com.enset.chatbotservice.agent.EnhancedAiAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final EnhancedAiAgent aiAgent;

    public ChatController(EnhancedAiAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return aiAgent.chat(message);
    }
}
