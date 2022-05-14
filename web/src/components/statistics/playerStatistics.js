import React, {useEffect, useState} from "react";
import {Button} from "../shared/button";
import {setNestedKey} from "../../helper-functions";

const asmStatisticsPlayersHref = "http://localhost:8080/statistics/players";

export function PlayerStatistics() {
    let [state, setState] = useState({
        data: {
            amateurSoccerGroupId: sessionStorage.getItem("amateurSoccerGroupId"),
            ranking: {
                playersRanking: []
            },
            pointsCriterion: {
                victories: 3,
                draws: 1,
                defeats: 0
            }
        }
    });

    useEffect(() => {
        calculateRanking(state, setState)
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

        <form onSubmit={(event) => handleSubmit(event, state, setState)}>
            <div className="row mb-3">
                <label htmlFor="player-statistics-victories" className="col-sm-2 col-form-label">Victories</label>
                <div className="col-sm-1">
                    <input name="pointsCriterion.victories"
                           type="text"
                           value={state.data.pointsCriterion.victories}
                           required
                           id="player-statistics-victories"
                           className="form-control"
                           onChange={(event) => handleInputChange(event, state, setState)}
                    />
                </div>
            </div>
            <div className="row mb-3">
                <label htmlFor="player-statistics-draws" className="col-sm-2 col-form-label">Draws</label>
                <div className="col-sm-1">
                    <input name="pointsCriterion.draws"
                           type="text"
                           value={state.data.pointsCriterion.draws}
                           id="player-statistics-draws"
                           className="form-control"
                           onChange={(event) => handleInputChange(event, state, setState)}
                    />
                </div>
            </div>
            <div className="row mb-3">
                <label htmlFor="player-statistics-defeats" className="col-sm-2 col-form-label">Defeats</label>
                <div className="col-sm-1">
                    <input name="pointsCriterion.defeats"
                           type="text"
                           value={state.data.pointsCriterion.defeats}
                           id="player-statistics-defeats"
                           className="form-control"
                           onChange={(event) => handleInputChange(event, state, setState)}
                    />
                </div>
            </div>

            <Button
                mode="add"
                type="submit"
                className="btn btn-success"
                text="Calculate Ranking"
            />
        </form>
    </main>;
}

const calculateRanking = (state, setState) => {
    let requestBody = {
        amateurSoccerGroupId: state.data.amateurSoccerGroupId,
        pointsCriterion: state.data.pointsCriterion
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

const handleSubmit = (e, state, setState) => {
    e.preventDefault();
    calculateRanking(state, setState)
}

const handleInputChange = (event, state, setState) => {
    const target = event.target;
    const name = "data." + target.name;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    let currentState = {...state};
    setNestedKey(currentState, name.split('.'), value);
    setState(currentState);
}
