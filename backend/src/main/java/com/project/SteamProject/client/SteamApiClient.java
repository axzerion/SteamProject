package com.project.SteamProject.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class SteamApiClient {

    private final String API_KEY = "0AB915FC1A9B99B45B6C547CD8823788";
    private final RestTemplate restTemplate = new RestTemplate();

    public int getCurrentPlayers(String appId) {
        String url = String.format("https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?key=%s&appid=%s", API_KEY, appId);
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