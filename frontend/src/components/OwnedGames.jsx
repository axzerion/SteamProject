import React, { useEffect, useState } from 'react';
import { getOwnedGames } from '../api/steamApi';

const steamId = '76561198047552050';

function OwnedGames() {
    const [games, setGames] = useState([]);

    useEffect(() => {
        getOwnedGames(steamId)
            .then(setGames)
            .catch(err => console.error("Failed to fetch owned games", err));
    }, []);

    return (
        <ul>
            {games.map(game => (
                <li key={game.appid}>
                    {game.name} — {game.playtimeMinutes} min played
                </li>
            ))}
        </ul>
    );
}

export default OwnedGames;
