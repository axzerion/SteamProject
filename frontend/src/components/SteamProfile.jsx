import React, { useEffect, useState } from 'react';
import axios from 'axios';

const steamId = '76561198047552050';

function SteamProfile() {
    const [profile, setProfile] = useState(null);

    useEffect(() => {
        axios.get(`/api/user/profile/${steamId}`)
            .then(res => setProfile(res.data))
            .catch(err => console.error("Failed to load Steam profile", err));
    }, []);

    if (!profile) return null;

    return (
        <div style={{ position: 'absolute', top: '1rem', right: '1rem', textAlign: 'right' }}>
            <img src={profile.avatarfull} alt="avatar" style={{ borderRadius: '50%', width: 64 }} />
            <div><strong>{profile.personaname}</strong></div>
        </div>
    );
}

export default SteamProfile;