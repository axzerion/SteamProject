package com.project.SteamProject.service;

import com.project.SteamProject.dto.GameInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SteamUserGameService {

    @Value("${steam.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public SteamUserGameService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GameInfo> getRecentlyPlayedGames(String steamId) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/")
                    .queryParam("key", apiKey)
                    .queryParam("steamid", steamId)
                    .queryParam("format", "json")
                    .toUriString();

            var response = restTemplate.getForObject(url, Map.class);
            return extractGameInfoList(response, "response", "games", false); // false = recent games

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.err.println("Steam API returned error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching recently played games: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Cacheable(value = "ownedGamesCache", key = "#steamId")
    public List<GameInfo> getOwnedGames(String steamId) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/")
                    .queryParam("key", apiKey)
                    .queryParam("steamid", steamId)
                    .queryParam("include_appinfo", "true")
                    .queryParam("include_played_free_games", "true")
                    .queryParam("format", "json")
                    .toUriString();

            var response = restTemplate.getForObject(url, Map.class);
            return extractGameInfoList(response, "response", "games", true); // true = owned games

        } catch (Exception e) {
            System.err.println("Error fetching owned games: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    private List<GameInfo> extractGameInfoList(Map<String, Object> root, String outerKey, String innerKey, boolean isOwned) {
        if (root == null || !root.containsKey(outerKey)) return Collections.emptyList();

        Map<String, Object> response = (Map<String, Object>) root.get(outerKey);
        List<Map<String, Object>> games = (List<Map<String, Object>>) response.get(innerKey);
        if (games == null) return Collections.emptyList();

        return games.stream().map(game -> {
            long appid = ((Number) game.get("appid")).longValue();
            String name = (String) game.getOrDefault("name", "Unknown Game");
            int playtime = ((Number) game.getOrDefault("playtime_forever", 0)).intValue();

            // Use img_icon_url for owned games
            String iconHash = isOwned ? (String) game.getOrDefault("img_icon_url", "") : "";
            String iconUrl = iconHash.isEmpty() ? "" :
                    "https://media.steampowered.com/steamcommunity/public/images/apps/" + appid + "/" + iconHash + ".jpg";

            // Use CDN-based header image for all logos
            String logoUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/" + appid + "/header.jpg";

            return new GameInfo(appid, name, playtime, iconUrl, logoUrl);
        }).collect(Collectors.toList());
    }
}
