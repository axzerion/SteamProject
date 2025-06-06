import React, { useEffect, useState } from 'react';
import { getRecentlyPlayed, getDefaultSteamId } from '../api/SteamAPI';

function RecentlyPlayed() {
    const [games, setGames] = useState([]);
    const [, setSteamId] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const id = await getDefaultSteamId();
                setSteamId(id);
                const fetchedGames = await getRecentlyPlayed(id);
                setGames(fetchedGames);
            } catch (err) {
                console.error("Failed to fetch recent games", err);
            }
        };
        fetchData();
    }, []);

    return (
        <ul style={{ listStyle: 'none', padding: 0 }}>
            {games.map(game => (
                <li
                    key={game.appid}
                    style={{
                        display: 'flex',
                        alignItems: 'center',
                        marginBottom: '12px',
                        gap: '12px'
                    }}
                >
                    {game.logoUrl && (
                        <img
                            src={game.logoUrl}
                            alt={`${game.name} header`}
                            style={{ height: '60px', borderRadius: '4px' }}
                            onError={(e) => { e.target.style.display = 'none'; }} // hide if invalid image
                        />
                    )}
                    <span>{game.name} — {(game.playtimeMinutes / 60).toFixed(1)} hrs</span>
                </li>
            ))}
        </ul>
    );
}

export default RecentlyPlayed;
