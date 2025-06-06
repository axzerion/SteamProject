import React, { useEffect, useState } from 'react';
import { getOwnedGames } from '../api/steamApi';

const steamId = '76561198047552050';

function OwnedGames() {
    const [games, setGames] = useState([]);

    useEffect(() => {
        getOwnedGames(steamId)
            .then(fetchedGames => {
                const sorted = [...fetchedGames].sort((a, b) => b.playtimeMinutes - a.playtimeMinutes);
                setGames(sorted);
            })
            .catch(err => console.error("Failed to fetch owned games", err));
    }, []);

    return (
        <div>
            <ul style={{ listStyle: 'none', paddingLeft: 0 }}>
                {games.map(game => (
                    <li key={game.appid} style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                        {game.iconUrl && (
                            <img
                                src={game.iconUrl}
                                alt={`${game.name} icon`}
                                style={{ height: '32px', width: '32px', marginRight: '10px', borderRadius: '4px' }}
                            />
                        )}
                        <span>{game.name} — {(game.playtimeMinutes / 60).toFixed(1)} hrs</span>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default OwnedGames;
