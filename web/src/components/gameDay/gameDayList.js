import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {extractId} from "../helper";

const asmGameDaysHref = 'http://localhost:8080/gameDays';

export function GameDayList() {
    const amateurSoccerGroupId = sessionStorage.getItem("amateurSoccerGroupId");
    const [state, setState] = useState({gameDays: []});

    useEffect(() => loadList(amateurSoccerGroupId, setState), []);

    return <div>
        <h1>Game Days</h1>
        <ul>
            {state.gameDays.map(gameDay => {
                    const gameDayId = extractId(gameDay._links.self.href);
                    return <li key={gameDay.date}>
                        <Link to={'/gameDays/' + gameDayId + '/view'}>{gameDay.date}</Link>
                        &nbsp;
                        <Link to={'/gameDays/' + gameDayId + '/edit'}>[edit]</Link>
                        &nbsp;
                        <a href='#'
                           onClick={(e) => removeGameDay(e, gameDayId, amateurSoccerGroupId, setState)}>[remove]</a>
                    </li>;
                }
            )}
        </ul>
    </div>;
}

function loadList(amateurSoccerGroupId, setState) {
    fetch(asmGameDaysHref + '/search/byAmateurSoccerGroupId?amateurSoccerGroupId=' + amateurSoccerGroupId, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
        .then(data => data._embedded.gameDays)
        .then(gameDays => setState({gameDays}))
}

async function removeGameDay(e, gameDayId, amateurSoccerGroupId, setState) {
    e.preventDefault();
    await fetch(asmGameDaysHref + "/" + gameDayId, {
        method: 'DELETE',
        mode: "cors"
    })
    loadList(amateurSoccerGroupId, setState);
}
