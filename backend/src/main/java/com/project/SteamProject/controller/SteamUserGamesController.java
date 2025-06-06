package com.project.SteamProject.controller;

import com.project.SteamProject.dto.GameInfo;
import com.project.SteamProject.service.SteamUserGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usergames")
public class SteamUserGamesController {

    @Autowired
    private SteamUserGameService gameService;

    @GetMapping("/recent/{steamId}")
    public List<GameInfo> getRecentlyPlayed(@PathVariable String steamId) {
        return gameService.getRecentlyPlayedGames(steamId);
    }

    @GetMapping("/owned/{steamId}")
    public List<GameInfo> getOwnedGames(@PathVariable String steamId) {
        return gameService.getOwnedGames(steamId);
    }

    @GetMapping("/test-cors")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("CORS is working");
    }
}

