package com.project.SteamProject.repository;

import com.project.SteamProject.model.PlayerCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerCountRepository extends JpaRepository<PlayerCount, Long> {
    List<PlayerCount> findByAppIdOrderByTimestampAsc(String appId);

    PlayerCount findTopByAppIdOrderByTimestampDesc(String appId);
}