import React, { useEffect, useState } from 'react';
import { getRecentlyPlayed } from '../api/steamApi';

const steamId = '76561198047552050';

function RecentlyPlayed() {
    const [games, setGames] = useState([]);

    useEffect(() => {
        getRecentlyPlayed(steamId)
            .then(setGames)
            .catch(err => console.error("Failed to fetch recent games", err));
    }, []);

    return (
        <ul>
            {games.map(game => (
                <li key={game.appid}>
                    {game.name} — {(game.playtimeMinutes / 60).toFixed(1)} hrs
                </li>
            ))}
        </ul>
    );
}

export default RecentlyPlayed;
