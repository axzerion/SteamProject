import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { getDefaultSteamId } from '../api/SteamAPI';

function SteamProfile() {
    const [profile, setProfile] = useState(null);
    const [, setSteamId] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const id = await getDefaultSteamId();
                setSteamId(id);
                const res = await axios.get(`/api/user/profile/${id}`);
                setProfile(res.data);
            } catch (err) {
                console.error("Failed to load Steam profile", err);
            }
        };
        fetchData();
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