import {useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";

const userDataPlayersHref = 'http://localhost:8081/players';

export function Player() {
    const {playerId} = useParams();
    const [state, setState] = useState({data: {nickname: ''}});

    const loadPlayer = () => {
        fetch(userDataPlayersHref + '/' + playerId, {
            method: 'GET',
            headers: {"Accept": "application/hal+json"},
            mode: "cors"
        }).then(response => response.json())
            .then(data => data)
            .then(data => setState({data}))
    }

    useEffect(loadPlayer, []);

    return <main>
        <h2>View Player</h2>
        <form>
            <div className="row mb-3">
                <label htmlFor="game-day-date" className="col-sm-2 col-form-label">Nickname</label>
                <div className="col-sm-10">
                    <input name="nickname"
                           type="text"
                           value={state.data.nickname}
                           required
                           id="game-day-date"
                           className="form-control"
                           readOnly={true}
                    />
                </div>
            </div>
        </form>
    </main>;
}