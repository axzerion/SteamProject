package com.project.SteamProject.service;

import com.project.SteamProject.model.PlayerCount;
import com.project.SteamProject.repository.PlayerCountRepository;
import com.project.SteamProject.client.SteamApiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class SteamStatsService {

    @Autowired
    private PlayerCountRepository repo;

    @Autowired
    private SteamApiClient client;

    @Value("${steam.app.ids}")
    private String appIdsCsv;

    @PostConstruct
    public void initFetch() {
        scheduledFetchAll(); // Immediate fetch at startup
    }

    // Automatically fetches stats every 600 seconds (10 min) for all listed App IDs
    @Scheduled(fixedRate = 300000)
    public void scheduledFetchAll() {
        List<String> appIds = Arrays.asList(appIdsCsv.split(","));
        for (String appId : appIds) {
            try {
                System.out.println("Fetching for appId: " + appId);
                fetchAndSave(appId.trim());
            } catch (Exception e) {
                System.err.println("Failed to fetch for " + appId + ": " + e.getMessage());
            }
        }
    }

    public PlayerCount fetchAndSave(String appId) {
        int count = client.getCurrentPlayers(appId);

        // Get the most recent record for this appId
        PlayerCount latest = repo.findTopByAppIdOrderByTimestampDesc(appId);

        // Only insert if player count changed
        if (latest == null || latest.getPlayerCount() != count) {
            PlayerCount pc = new PlayerCount();
            pc.setAppId(appId);
            pc.setPlayerCount(count);
            pc.setTimestamp(LocalDateTime.now());
            return repo.save(pc);
        }

        System.out.println("No change in player count for appId " + appId + ", skipping insert.");
        return latest;
    }

    public List<PlayerCount> getHistory(String appId) {
        return repo.findByAppIdOrderByTimestampAsc(appId);
    }

    public List<PlayerCount> getAllHistory() {
        return repo.findAll();
    }
}
