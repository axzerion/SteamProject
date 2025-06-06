package com.project.SteamProject.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class SteamApiClient {

    @Value("${steam.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public SteamApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int getCurrentPlayers(String appId) {
        String url = String.format(
                "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?key=%s&appid=%s",
                apiKey, appId
        );
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response != null && response.containsKey("response")) {
            Map<String, Object> inner = (Map<String, Object>) response.get("response");
            if (inner.containsKey("player_count")) {
                return (int) inner.get("player_count");
            }
        }
        return 0;
    }
}
