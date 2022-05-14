import {useEffect, useState} from "react";

const asmStatisticsPlayersHref = "http://localhost:8080/statistics/players";

export function PlayerStatistics() {
    const [state, setState] = useState({
        data: {
            amateurSoccerGroupId: sessionStorage.getItem("amateurSoccerGroupId"),
            ranking: {
                playersRanking: []
            }
        }
    });

    useEffect(() => {
        calculateRanking(state.data.amateurSoccerGroupId, setState)
    }, []);

    return <main>
        <h1>Player Statistics</h1>

        <table className="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Player</th>
                <th scope="col">Classification</th>
                <th scope="col">Points</th>
                <th scope="col">Matches</th>
                <th scope="col">Victories</th>
                <th scope="col">Draws</th>
                <th scope="col">Defeats</th>
                <th scope="col">Goals in Favor</th>
                <th scope="col">Goals Against</th>
                <th scope="col">Balance</th>
            </tr>
            </thead>
            <tbody>
            {state.data.ranking.playersRanking.map((playerRanking, index) =>
                <tr key={index}>
                    <th scope="row">{playerRanking.position}</th>
                    <td>{playerRanking.playerId}</td>
                    <td>{playerRanking.classification}</td>
                    <td>{playerRanking.points}</td>
                    <td>{playerRanking.matches}</td>
                    <td>{playerRanking.victories}</td>
                    <td>{playerRanking.draws}</td>
                    <td>{playerRanking.defeats}</td>
                    <td>{playerRanking.goalsInFavor}</td>
                    <td>{playerRanking.goalsAgainst}</td>
                    <td>{playerRanking.balance}</td>
                </tr>
            )}
            </tbody>
        </table>
    </main>;
}

function calculateRanking(amateurSoccerGroupId, setState) {
    let requestBody = {
        amateurSoccerGroupId: amateurSoccerGroupId,
        pointsCriterion: {
            victories: 3,
            draws: 1,
            defeats: 0
        }
    }
    fetch(asmStatisticsPlayersHref, {
        method: 'POST',
        headers: {"Content-Type": "application/hal+json"},
        mode: 'cors',
        body: JSON.stringify(requestBody)
    })
        .then(response => response.json())
        .then(ranking => setState((s) => ({
            ...s, data: {...s.data, ranking}
        })));
}
