import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/usergames';

export const getPlayerCounts = async (appId, refresh = false) => {
    const response = await axios.get(`/api/playercount/${appId}`, {
        params: { refresh }
    });
    return response.data;
};

export const getRecentlyPlayed = async (steamId) => {
    const res = await axios.get(`${API_BASE}/recent/${steamId}`);
    return res.data;
};

export const getOwnedGames = async (steamId) => {
    const res = await axios.get(`${API_BASE}/owned/${steamId}`);
    return res.data;
};
