package com.project.SteamProject.controller;

import com.project.SteamProject.model.PlayerCount;
import com.project.SteamProject.service.SteamStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playercount")
public class SteamStatsController {

    @Autowired
    private SteamStatsService service;

    @GetMapping("/{appId}")
    public List<PlayerCount> getStats(
            @PathVariable String appId,
            @RequestParam(defaultValue = "false") boolean refresh) {
        if (refresh) {
            service.fetchAndSave(appId);
        }
        return service.getHistory(appId);
    }

    @GetMapping("/all")
    public List<PlayerCount> getAllStats() {
        return service.getAllHistory();
    }
}
