import React from 'react';
import { Line } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

function PlayerChart({ data }) {
    const chartData = {
        labels: data.map(p => new Date(p.timestamp).toLocaleString()),
        datasets: [
            {
                label: 'Concurrent Players',
                data: data.map(p => p.playerCount),
                fill: false,
                tension: 0.1,
            },
        ],
    };

    return <Line data={chartData} />;
}

export default PlayerChart;
