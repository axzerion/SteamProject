import axios from 'axios';


export const getPlayerCounts = async (appId, refresh = false) => {
    const response = await axios.get(`/api/playercount/${appId}`, {
        params: { refresh }
    });
    return response.data;
};
