package com.project.SteamProject.dto;

public record GameInfo(
        long appid,
        String name,
        int playtimeMinutes,
        String iconUrl,
        String logoUrl
) {}