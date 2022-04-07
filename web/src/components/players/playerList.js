import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {extractId} from "../helper";

const userDataPlayersHref = 'http://localhost:8081/players';

export function PlayerList() {
    const [state, setState] = useState({players: []});

    useEffect(() => loadList(setState), []);

    return <main>
        <h2>Players</h2>
        <ul>
            {state.players.map((player, index) =>
                <li key={index}>
                    <Link to={'/players/' + extractId(player._links.self.href)}>{player.nickname}</Link>
                    &nbsp;
                    <a href='#' onClick={(e) => removePlayer(e, player._links.self.href, setState)}>[remove]</a>
                </li>
            )}
        </ul>
    </main>;
}

function loadList(setState) {
    const amateurSoccerGroupId = sessionStorage.getItem('amateurSoccerGroupId');
    fetch(userDataPlayersHref + '/search/byAmateurSoccerGroupId?amateurSoccerGroupId=' + amateurSoccerGroupId, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    }).then(response => response.json())
        .then(data => data._embedded.players)
        .then(players => setState({players}))
};

async function removePlayer(event, playerUri, setState) {
    await fetch(playerUri, {
        method: 'DELETE',
        headers: {'Content-type': 'application/hal+json'},
    })
    loadList(setState);
}