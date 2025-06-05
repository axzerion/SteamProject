import {useEffect, useState} from "react";
import PlayerChart from "../components/PlayerChart.jsx";
import RecentlyPlayed from "../components/RecentlyPlayed.jsx";
import OwnedGames from "../components/OwnedGames.jsx";
import {getPlayerCounts} from "../api/SteamAPI.js";

function HomePage() {
    const [allData, setAllData] = useState([]);
    const [appId, setAppId] = useState('730');

    const gameNames = {
        '730': 'CS:GO',
        '570': 'Dota 2',
        '1172470': 'Apex Legends'
    };

    useEffect(() => {
        const fetchAll = async () => {
            try {
                const [csgo, dota2, apex] = await Promise.all([
                    getPlayerCounts('730'),
                    getPlayerCounts('570'),
                    getPlayerCounts('1172470')
                ]);
                setAllData([...csgo, ...dota2, ...apex]);
            } catch (e) {
                console.error('Error fetching game stats:', e);
            }
        };

        fetchAll();
    }, []);

    const handleAppIdChange = (e) => {
        setAppId(e.target.value);
    };

    const filteredData = allData.filter(entry => entry.appId === appId);

    return (
        <div style={{ padding: '2rem' }}>
            <h1>Steam Player Count for {gameNames[appId]}</h1>
            <select value={appId} onChange={handleAppIdChange}>
                <option value="730">CS:GO</option>
                <option value="570">Dota 2</option>
                <option value="1172470">Apex Legends</option>
            </select>

            <div style={{ height: '300px', marginTop: '1rem' }}>
                <PlayerChart data={filteredData} />
            </div>

            <div style={{ display: 'flex', marginTop: '2rem', gap: '2rem' }}>
                <div style={{ flex: 1 }}>
                    <h2>Owned Games</h2>
                    <OwnedGames />
                </div>
                <div style={{ flex: 1 }}>
                    <h2>Recently Played Games</h2>
                    <RecentlyPlayed />
                </div>
            </div>
        </div>
    );
}

export default HomePage;