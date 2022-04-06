import React, {useEffect, useState} from "react";

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
                    {player.nickname}
                </li>
            )}
        </ul>
    </main>;
}