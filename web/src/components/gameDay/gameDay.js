import React, {useEffect, useState} from "react";
import {getNestedValue, setNestedKey} from "../../helper-functions";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {extractId} from "../helper";
import {Button} from "../shared/button";
import {useNavigate, useParams} from "react-router-dom";
import {v4 as uuidV4} from "uuid";

const titles = {'add': 'Create', 'edit': 'Edit', 'view': 'View'}

const asmGameDaysHref = "http://localhost:8080/gameDays";
const userDataGameDaysHref = "http://localhost:8081/gameDays";
const userDataPlayersDaysHref = "http://localhost:8081/players";

export function GameDay(props) {
    const gameDayId = useParams().gameDayId || uuidV4();
    const [state, setState] = useState({
        data: {
            amateurSoccerGroupId: sessionStorage.getItem("amateurSoccerGroupId"),
            date: '',
            quote: '',
            author: '',
            description: '',
            matches: [],
        },
        players: [],
    });
    const navigate = useNavigate();

    useEffect(() => {
        fetchUserDataPlayers(state, setState)
        fetchGameDay(gameDayId, state, setState)
    }, []);

    const title = titles[props.mode];
    return (
        <div>
            <ToastContainer
                position="top-right"
                autoClose={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
            />
            <h1>{title} Game Day</h1>
            <form onSubmit={(event) => handleSubmit(event, gameDayId, state, navigate)}>
                <div className="row mb-3">
                    <label htmlFor="game-day-date" className="col-sm-2 col-form-label">Date</label>
                    <div className="col-sm-10">
                        <input name="date"
                               type="date"
                               value={state.data.date}
                               required
                               id="game-day-date"
                               className="form-control"
                               onChange={(event) => handleInputChange(event, state, setState)}
                               readOnly={props.mode === 'view'}
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="game-day-quote" className="col-sm-2 col-form-label">Quote</label>
                    <div className="col-sm-10">
                        <input name="quote"
                               type="text"
                               value={state.data.quote}
                               id="game-day-quote"
                               className="form-control"
                               onChange={(event) => handleInputChange(event, state, setState)}
                               readOnly={props.mode === 'view'}
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="game-day-author" className="col-sm-2 col-form-label">Author</label>
                    <div className="col-sm-10">
                        <input name="author"
                               type="text"
                               value={state.data.author}
                               id="game-day-author"
                               className="form-control"
                               onChange={(event) => handleInputChange(event, state, setState)}
                               readOnly={props.mode === 'view'}
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="game-day-description" className="col-sm-2 col-form-label">Description</label>
                    <div className="col-sm-10">
                            <textarea name="description"
                                      value={state.data.description}
                                      id="game-day-description"
                                      className="form-control"
                                      onChange={(event) => handleInputChange(event, state, setState)}
                                      readOnly={props.mode === 'view'}
                            />
                    </div>
                </div>
                <ul>
                    {state.data.matches
                        .sort((a, b) => a.order > b.order ? 1 : (a.order === b.order ? 0 : -1))
                        .map((match, index) => <li key={index}>
                            <Match
                                prefix={"matches." + index}
                                data={match}
                                handleInputChange={(event) => handleInputChange(event, state, setState)}
                                handleRemoveMatch={handleRemoveMatch}
                                handleRemovePlayer={() => handleRemovePlayer(state, setState)}
                                mode={props.mode}
                                players={state.players}
                            />
                        </li>)}
                </ul>

                <Button
                    mode={props.mode}
                    type="button"
                    className="btn btn-primary"
                    onClick={(e) => {
                        e.preventDefault();
                        handleAddMatch(state, setState);
                    }}
                    text="Add Another Match"
                />

                <Button
                    mode={props.mode}
                    type="submit"
                    className="btn btn-success"
                    text={title + " Game Day"}
                />
            </form>
        </div>
    );
}

function Match(props) {
    let nicknamesByPlayerId = (props.players || []).reduce((map, obj) => {
        map[obj.id] = obj.nickname
        return map;
    }, {});
    let comparator = (playerStatisticA, playerStatisticB) => {
        let nicknameA = nicknamesByPlayerId[playerStatisticA.playerId] || '';
        let nicknameB = nicknamesByPlayerId[playerStatisticB.playerId] || '';
        if (nicknameA < nicknameB) return -1;
        if (nicknameA > nicknameB) return 1;
        return 0;
    }

    return (
        <div>
            <h2>
                Match #{props.data.order}
                <Button
                    mode={props.mode}
                    type="submit"
                    className="btn btn-primary"
                    onClick={(e) => {
                        e.preventDefault();
                        props.handleRemoveMatch(props.data.order);
                    }}
                    text="Remove"
                />
            </h2>
            <table className='table'>
                <thead>
                <tr>
                    <th scope='col'>Nickname</th>
                    <th scope='col'>Team</th>
                    <th scope='col'>Goals in Favor</th>
                    <th scope='col'>Goals Against</th>
                    <th scope='col'>Yellow Cards</th>
                    <th scope='col'>Blue Cards</th>
                    <th scope='col'>Red Cards</th>
                </tr>
                </thead>
                <tbody>
                {props.data.playerStatistics.sort(comparator).map((playerStatistic, index) =>
                    <PlayerStatistic
                        key={index}
                        index={index}
                        prefix={props.prefix + ".playerStatistics." + index}
                        data={playerStatistic}
                        handleInputChange={props.handleInputChange}
                        handleRemovePlayer={props.handleRemovePlayer}
                        mode={props.mode}
                        disableRemovePlayerButton={props.data.playerStatistics.length < 3}
                        players={props.players}
                    />
                )}
                </tbody>
            </table>
        </div>
    );
}

function PlayerStatistic(props) {
    if (props.mode === 'view' && !['A', 'B'].includes(props.data.team)) {
        return null;
    }
    const prefix = props.prefix + ".";
    return (
        <tr key={props.index}>
            <td>
                <PlayerNickname
                    prefix={prefix}
                    playerId={props.data.playerId}
                    handleInputChange={props.handleInputChange}
                    mode={props.mode}
                    players={props.players}
                />
            </td>
            <td className='col-3'>
                <div className="form-check form-check-inline">
                    <label className="form-check-label">
                        <input className="form-check-input"
                               type="radio"
                               name={prefix + "team"}
                               value="A"
                               checked={props.data.team === 'A'}
                               onChange={props.handleInputChange}
                               disabled={props.mode === 'view'}
                        />
                        A
                    </label>
                </div>
                <div className="form-check form-check-inline">
                    <label className="form-check-label">
                        <input className="form-check-input"
                               type="radio"
                               name={prefix + "team"}
                               value="B"
                               checked={props.data.team === 'B'}
                               onChange={props.handleInputChange}
                               disabled={props.mode === 'view'}
                        />
                        B
                    </label>
                </div>
                <div className="form-check form-check-inline">
                    <label className="form-check-label">
                        <input className="form-check-input"
                               type="radio"
                               name={prefix + "team"}
                               value=""
                               checked={props.data.team === ''}
                               onChange={props.handleInputChange}
                               disabled={props.mode === 'view'}
                        />
                        N/A
                    </label>
                </div>
            </td>
            <td className='col-1'>
                <input name={prefix + "goalsInFavor"}
                       type="number"
                       value={props.data.goalsInFavor}
                       min="0"
                       max="99"
                       className="form-control"
                       onChange={props.handleInputChange}
                       readOnly={props.mode === 'view'}
                />
            </td>
            <td className='col-1'>
                <input name={prefix + "goalsAgainst"}
                       type="number"
                       value={props.data.goalsAgainst}
                       min="0"
                       max="99"
                       className="form-control"
                       onChange={props.handleInputChange}
                       readOnly={props.mode === 'view'}
                />
            </td>
            <td className='col-1'>
                <input name={prefix + "yellowCards"}
                       type="number"
                       value={props.data.yellowCards}
                       min="0"
                       max="99"
                       className="form-control"
                       onChange={props.handleInputChange}
                       readOnly={props.mode === 'view'}
                />
            </td>
            <td className='col-1'>
                <input name={prefix + "blueCards"}
                       type="number"
                       value={props.data.blueCards}
                       min="0"
                       max="99"
                       className="form-control"
                       onChange={props.handleInputChange}
                       readOnly={props.mode === 'view'}
                />
            </td>
            <td className='col-1'>
                <input name={prefix + "redCards"}
                       type="number"
                       value={props.data.redCards}
                       min="0"
                       max="99"
                       className="form-control"
                       onChange={props.handleInputChange}
                       readOnly={props.mode === 'view'}
                />
            </td>
        </tr>
    );
}

function PlayerNickname(props) {
    return props.players
        .filter(player => player.id === props.playerId)
        .map(player => player.nickname)
        [0] || ''
}

const fetchUserDataPlayers = (state, setState) => {
    function map(players) {
        return (players || []).map(player => ({
            id: extractId(player._links.player.href) || '',
            userId: player.userId || '',
            nickname: player.nickname || '',
        }));
    }

    fetch(userDataPlayersDaysHref + "/search/byAmateurSoccerGroupId?amateurSoccerGroupId=" + state.data.amateurSoccerGroupId, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
        .then(json => json._embedded || {players: []})
        .then(embedded => map(embedded.players))
        .then(players => setState((s) => ({...s, players})))
}

const fetchGameDay = (gameDayId, state, setState) => {
    Promise.all([
        fetchAsmGameDay(gameDayId),
        fetchUserDataGameDay(gameDayId),
    ]).then(([asmGameDay, userDataGameDay]) => {
        asmGameDay.matches.forEach(match => {
            const playersWithStatistics = match.playerStatistics.map(playerStatistics => playerStatistics.playerId);
            const playersWithoutStatistics = state.players.map(player => player.id).filter(playerId => !playersWithStatistics.includes(playerId));
            playersWithoutStatistics.forEach(playerId => match.playerStatistics.push(createEmptyPlayer(playerId)))
        })
        setState((s) => ({...s, data: {...asmGameDay, ...userDataGameDay}}));
    });
}

const fetchAsmGameDay = (gameDayId) => {
    return fetch(asmGameDaysHref + "/" + gameDayId, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
        .then(json => ({
            amateurSoccerGroupId: sessionStorage.getItem("amateurSoccerGroupId"),
            date: json.date || '',
            quote: json.quote || '',
            author: json.author || '',
            description: json.description || '',
            matches: json.matches || [],
        }));
}

const fetchUserDataGameDay = (gameDayId) => {
    return fetch(userDataGameDaysHref + "/" + gameDayId, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
        .then(json => ({
            quote: json.quote || "",
            author: json.author || "",
            description: json.description || "",
        }));
}

const handleInputChange = (event, state, setState) => {
    const target = event.target;
    const name = "data." + target.name;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    let currentState = {...state};
    setNestedKey(currentState, name.split('.'), value);
    setState(currentState);
}

const handleAddMatch = (state, setState) => {
    let currentState = {...state};
    const path = ("data.matches").split('.');
    let value = getNestedValue(currentState, path);
    value.push(createEmptyMatch(value.length + 1, state.players));
    setNestedKey(currentState, path, value);
    setState(currentState);
}

const handleRemoveMatch = (order, state, setState) => {
    let currentState = {...state};
    const value = state.data.matches.filter(value => value.order !== order);
    value.map((match, index) => match.order = index + 1)
    currentState.data.matches = value
    setState({currentState});
}

const handleRemovePlayer = (state, setState) => (key) => {
    let currentState = {...state};
    const path = ("data." + key).split('.');
    const playerToRemove = getNestedValue(currentState, path);
    let playerStatistics = path.slice(0, path.length - 2);
    playerStatistics.push("playerStatistics");
    let value = getNestedValue(currentState, playerStatistics)
        .filter(playerStatistic => playerToRemove.playerId !== playerStatistic.playerId);
    setNestedKey(currentState, playerStatistics, value);
    setState(currentState);
}

const handleSubmit = (e, gameDayId, state, navigate) => {
    e.preventDefault();
    Promise.all([
        saveAsmGameDay(gameDayId, state),
        saveUserDataGameDay(gameDayId, state),
    ]).then(([asmGameDaySuccess, userDataGameDaySuccess]) => {
        if (asmGameDaySuccess && userDataGameDaySuccess) {
            navigate('/gameDays')
        }
    });
}

const saveAsmGameDay = (gameDayId, state) => {
    const requestBody = {
        amateurSoccerGroupId: state.data.amateurSoccerGroupId,
        date: state.data.date,
        matches: state.data.matches.map(match => ({
            order: match.order,
            playerStatistics: match.playerStatistics.filter(playerStatistics => ['A', 'B'].includes(playerStatistics.team))
        })),
    }
    return fetch(asmGameDaysHref + "/" + gameDayId, {
        method: 'PUT',
        headers: {"content-type": "application/hal+json"},
        body: JSON.stringify(requestBody)
    }).then((response) => {
        switch (response.status) {
            case 200:
            case 201:
                return true;
            case 400:
                response.json().then(body => body.violations
                    .map(violation => toast.warn("field: " + violation.field + ", message: " + violation.message)));
                break;
            case 404:
            case 500:
                response.json().then(body => toast.error("title: " + body.title + ", detail: " + body.detail))
                break;
        }
        return false;
    })
}

const saveUserDataGameDay = (gameDayId, state) => {
    const requestBody = {
        quote: state.data.quote,
        author: state.data.author,
        description: state.data.description,
    }
    return fetch(userDataGameDaysHref + "/" + gameDayId, {
        method: 'PUT',
        headers: {'content-type': 'application/hal+json'},
        body: JSON.stringify(requestBody)
    }).then(response => {
        switch (response.status) {
            case 200:
            case 201:
                return true;
        }
        return false;
    })
}

const createEmptyMatch = (order, players) => {
    return {
        order: order,
        playerStatistics: players.map(player => createEmptyPlayer(player.id)),
    };
}

const createEmptyPlayer = (playerId) => {
    return {
        playerId: playerId,
        userId: '',
        team: '',
        goalsInFavor: '',
        goalsAgainst: '',
        yellowCards: '',
        blueCards: '',
        redCards: ''
    };
}