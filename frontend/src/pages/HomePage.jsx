import { useEffect, useState } from "react";
import PlayerChart from "../components/PlayerChart.jsx";
import RecentlyPlayed from "../components/RecentlyPlayed.jsx";
import OwnedGames from "../components/OwnedGames.jsx";
import { getPlayerCounts } from "../api/SteamAPI.js";
import SteamProfile from '../components/SteamProfile';
import './HomePage.css'; // Ensure this is included!

function HomePage() {
    const [allData, setAllData] = useState([]);
    const [appId, setAppId] = useState('730');

    const gameNames = {
        '730': 'CS2',
        '570': 'Dota 2',
        '1172470': 'Apex Legends'
    };

    useEffect(() => {
        const fetchAll = async () => {
            try {
                const [cs2, dota2, apex] = await Promise.all([
                    getPlayerCounts('730'),
                    getPlayerCounts('570'),
                    getPlayerCounts('1172470')
                ]);
                setAllData([...cs2, ...dota2, ...apex]);
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
        <div className="page-container">
            {/* TOP RIGHT PROFILE */}
            <div style={{ position: 'absolute', top: 0, right: 0, padding: '1rem' }}>
                <SteamProfile />
            </div>

            <h1>Steam Player Count for {gameNames[appId]}</h1>

            <select value={appId} onChange={handleAppIdChange}>
                <option value="730">CS2</option>
                <option value="570">Dota 2</option>
                <option value="1172470">Apex Legends</option>
            </select>

            <div className="player-chart-container">
                <PlayerChart data={filteredData} />
            </div>

            <div className="games-section">
                <div>
                    <h2>Owned Games</h2>
                    <OwnedGames />
                </div>
                <div>
                    <h2>Recently Played Games</h2>
                    <RecentlyPlayed />
                </div>
            </div>
        </div>
    );
}

export default HomePage;
