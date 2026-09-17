package com.fisioroyale.leads.web;

import com.fisioroyale.leads.dto.DashboardResponse;
import com.fisioroyale.leads.model.LeadStatus;
import com.fisioroyale.leads.service.LeadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {
    private final LeadService service;

    public DashboardController(LeadService service) { this.service = service; }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() { return service.dashboard(); }

    @GetMapping("/pipeline/status")
    public List<String> status() { return Arrays.stream(LeadStatus.values()).map(Enum::name).toList(); }
}
