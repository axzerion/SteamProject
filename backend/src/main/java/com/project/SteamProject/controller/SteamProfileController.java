package com.project.SteamProject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
public class SteamProfileController {

    @Value("${steam.api.key}")
    private String apiKey;

    @Value("${steam.default.id}")
    private String defaultSteamId;

    private final RestTemplate restTemplate;

    public SteamProfileController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/api/user/default-id")
    public String getDefaultSteamId() {
        return defaultSteamId;
    }

    @GetMapping("/api/user/profile/{steamId}")
    public Map<String, Object> getSteamProfile(@PathVariable String steamId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/")
                .queryParam("key", apiKey)
                .queryParam("steamids", steamId)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        return (Map<String, Object>) ((List<?>) ((Map<?, ?>) response.get("response")).get("players")).get(0);
    }
}
