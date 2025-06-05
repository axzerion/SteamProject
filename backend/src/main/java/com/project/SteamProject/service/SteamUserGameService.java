package com.project.SteamProject.service;

import com.project.SteamProject.dto.GameInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SteamUserGameService {

    @Value("${steam.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<GameInfo> getRecentlyPlayedGames(String steamId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/")
                .queryParam("key", apiKey)
                .queryParam("steamid", steamId)
                .queryParam("format", "json")
                .toUriString();

        var response = restTemplate.getForObject(url, Map.class);
        return extractGameInfoList(response, "response", "games");
    }

    public List<GameInfo> getOwnedGames(String steamId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/")
                .queryParam("key", apiKey)
                .queryParam("steamid", steamId)
                .queryParam("include_appinfo", "true")
                .queryParam("include_played_free_games", "true")
                .queryParam("format", "json")
                .toUriString();

        var response = restTemplate.getForObject(url, Map.class);
        return extractGameInfoList(response, "response", "games");
    }

    @SuppressWarnings("unchecked")
    private List<GameInfo> extractGameInfoList(Map<String, Object> root, String outerKey, String innerKey) {
        Map<String, Object> response = (Map<String, Object>) root.get(outerKey);
        List<Map<String, Object>> games = (List<Map<String, Object>>) response.get(innerKey);

        if (games == null) return Collections.emptyList();

        return games.stream().map(game -> {
            long appid = ((Number) game.get("appid")).longValue();
            String name = (String) game.getOrDefault("name", "Unknown Game");
            int playtime = ((Number) game.getOrDefault("playtime_forever", 0)).intValue();
            String icon = (String) game.getOrDefault("img_icon_url", "");
            String logo = (String) game.getOrDefault("img_logo_url", "");

            String iconUrl = icon.isEmpty() ? "" :
                    "http://media.steampowered.com/steamcommunity/public/images/apps/" + appid + "/" + icon + ".jpg";
            String logoUrl = logo.isEmpty() ? "" :
                    "http://media.steampowered.com/steamcommunity/public/images/apps/" + appid + "/" + logo + ".jpg";

            return new GameInfo(appid, name, playtime, iconUrl, logoUrl);
        }).collect(Collectors.toList());
    }
}
