import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {extractId} from "../helper";

const userDataPlayersHref = 'http://localhost:8081/players';

export function PlayerList() {
    const amateurSoccerGroupId = sessionStorage.getItem('amateurSoccerGroupId');
    const [state, setState] = useState({players: []});

    const loadList = () => {
        fetch(userDataPlayersHref + '/search/byAmateurSoccerGroupId?amateurSoccerGroupId=' + amateurSoccerGroupId, {
            method: 'GET',
            headers: {"Accept": "application/hal+json"},
            mode: "cors"
        }).then(response => response.json())
            .then(data => data._embedded.players)
            .then(players => setState({players}))
    };

    useEffect(loadList, []);

    return <main>
        <h2>Players</h2>
        <ul>
            {state.players.map((player, index) =>
                <li key={index}>
                    <Link to={'/players/' + extractId(player._links.self.href)}>{player.nickname}</Link>
                </li>
            )}
        </ul>
    </main>;
}