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
    const labels = data.map(p => new Date(p.timestamp).toLocaleTimeString());
    const lastIndex = labels.length - 1;

    const chartData = {
        labels,
        datasets: [
            {
                label: 'Concurrent Players',
                data: data.map(p => p.playerCount),
                fill: false,
                tension: 0.2,
                borderColor: '#00FFFF',
                backgroundColor: '#00FFFF',
                pointBackgroundColor: '#00FFFF',
                pointHoverRadius: 6,
                pointRadius: 3,
                borderWidth: 2,
            },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            x: {
                ticks: {
                    autoSkip: false,
                    callback: function (_, index) {
                        return index % 6 === 0 || index === lastIndex ? labels[index] : '';
                    },
                    color: '#FFFFFF', // Brighter ticks
                },
                title: {
                    display: true,
                    text: 'Time',
                    color: '#FFFFFF',
                },
                grid: {
                    color: '#444', // Grid lines
                }
            },
            y: {
                ticks: {
                    color: '#FFFFFF',
                },
                title: {
                    display: true,
                    text: 'Player Count',
                    color: '#FFFFFF',
                },
                grid: {
                    color: '#444',
                }
            }
        },
        plugins: {
            legend: {
                position: 'top',
                labels: {
                    color: '#FFFFFF',
                }
            },
            title: {
                display: false,
            },
            tooltip: {
                backgroundColor: '#222',
                titleColor: '#00FFFF',
                bodyColor: '#FFFFFF',
            },
        },
    };

    return <Line data={chartData} options={options} />;
}

export default PlayerChart;
