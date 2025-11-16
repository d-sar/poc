package com.enset.mcpserver.tools;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mcp")
public class McpController {

    private final McpService mcpService;

    public McpController(McpService mcpService) {
        this.mcpService = mcpService;
    }

    @GetMapping("/beneficiaries")
    public List<Map<String, Object>> listBeneficiaries() {
        return mcpService.getBeneficiaries();
    }

    @GetMapping("/virements")
    public List<Map<String, Object>> listVirements(@RequestParam String beneficiary) {
        return mcpService.getVirements(beneficiary);
    }
}
