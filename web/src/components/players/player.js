import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {setNestedKey} from "../../helper-functions";
import {Button} from "../shared/button";
import {v4 as uuidV4} from 'uuid';

const userDataPlayersHref = 'http://localhost:8081/players';
const titles = {'add': 'Create', 'edit': 'Edit', 'view': 'View'}

export function Player(props) {
    const playerId = useParams().playerId || uuidV4();
    const [state, setState] = useState({data: {nickname: ''}});
    const navigate = useNavigate();
    const title = titles[props.mode];

    useEffect(() => loadPlayer(playerId, props.mode, setState), []);

    return <main>
        <h2>{title} Player</h2>
        <form>
            <div className="row mb-3">
                <label htmlFor="player-nickname" className="col-sm-2 col-form-label">Nickname</label>
                <div className="col-sm-10">
                    <input name="nickname"
                           type="text"
                           value={state.data.nickname}
                           required
                           id="player-nickname"
                           onChange={(event) => handleInputChange(event, state, setState)}
                           className="form-control"
                    />
                </div>
            </div>
            <Button
                mode={props.mode}
                type="submit"
                className="btn btn-success"
                onClick={(event) => handleSubmit(event, playerId, state, navigate)}
                text={title + " Player"}
            />
        </form>
    </main>;
}

const loadPlayer = (playerId, mode, setState) => {
    if (mode !== 'add') {
        fetch(userDataPlayersHref + '/' + playerId, {
            method: 'GET',
            headers: {"Accept": "application/hal+json"},
            mode: "cors"
        }).then(response => response.json())
            .then(data => data)
            .then(data => setState({data}))
    }
}

const handleInputChange = (event, state, setState) => {
    const target = event.target;
    const name = "data." + target.name;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    let currentState = {...state};
    setNestedKey(currentState, name.split('.'), value);
    setState(currentState);
}

const handleSubmit = (event, playerId, state, navigate) => {
    event.preventDefault();
    const requestBody = {
        amateurSoccerGroupId: sessionStorage.getItem('amateurSoccerGroupId'),
        nickname: state.data.nickname
    }
    fetch(userDataPlayersHref + '/' + playerId, {
        method: 'PUT',
        headers: {'Content-type': 'application/hal+json'},
        body: JSON.stringify(requestBody)
    }).then(response => {
        switch (response.status) {
            case 200:
            case 201:
                navigate("/players")
                return true;
        }
        return false;
    })
}