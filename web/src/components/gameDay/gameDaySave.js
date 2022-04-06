import React from "react";
import {getNestedValue, setNestedKey} from "../../helper-functions";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {extractId} from "../helper";
import {v4 as uuidV4} from 'uuid';
import {Navigate, useParams} from "react-router-dom";

const titles = {'add': 'Create', 'edit': 'Edit', 'view': 'View'}

const asmGameDaysHref = "http://localhost:8080/gameDays";
const userDataGameDaysHref = "http://localhost:8081/gameDays";
const userDataPlayersDaysHref = "http://localhost:8081/players";

export function GameDayNew() {
    return (<GameDaySave gameDayId={uuidV4()} mode='add'/>);
}

export function GameDayEdit() {
    const params = useParams();
    return <GameDaySave gameDayId={params.gameDayId} mode='edit'/>;
}

export function GameDayView() {
    const params = useParams();
    return (<GameDaySave gameDayId={params.gameDayId} mode='view'/>);
}

class GameDaySave extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            redirect: false,
            data: {
                amateurSoccerGroupId: sessionStorage.getItem("amateurSoccerGroupId"),
                date: '',
                quote: '',
                author: '',
                description: '',
                matches: [],
            },
            players: [],
        }
    }

    componentDidMount() {
        this.fetchUserDataPlayers(sessionStorage.getItem("amateurSoccerGroupId"))
            .then(players => this.setState({players: players}))
        if (this.props.mode !== 'add') {
            this.fetchGameDay()
        }
    }

    render() {
        const title = titles[this.props.mode];
        const {redirect} = this.state;
        return (
            <main>
                {redirect && <Navigate to='/gameDays'/>}
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
                <form>
                    <div className="row mb-3">
                        <label htmlFor="game-day-date" className="col-sm-2 col-form-label">Date</label>
                        <div className="col-sm-10">
                            <input name="date"
                                   type="date"
                                   value={this.state.data.date}
                                   required
                                   id="game-day-date"
                                   className="form-control"
                                   onChange={this.handleInputChange}
                                   readOnly={this.props.mode === 'view'}
                            />
                        </div>
                    </div>
                    <div className="row mb-3">
                        <label htmlFor="game-day-quote" className="col-sm-2 col-form-label">Quote</label>
                        <div className="col-sm-10">
                            <input name="quote"
                                   type="text"
                                   value={this.state.data.quote}
                                   id="game-day-quote"
                                   className="form-control"
                                   onChange={this.handleInputChange}
                                   readOnly={this.props.mode === 'view'}
                            />
                        </div>
                    </div>
                    <div className="row mb-3">
                        <label htmlFor="game-day-author" className="col-sm-2 col-form-label">Author</label>
                        <div className="col-sm-10">
                            <input name="author"
                                   type="text"
                                   value={this.state.data.author}
                                   id="game-day-author"
                                   className="form-control"
                                   onChange={this.handleInputChange}
                                   readOnly={this.props.mode === 'view'}
                            />
                        </div>
                    </div>
                    <div className="row mb-3">
                        <label htmlFor="game-day-description" className="col-sm-2 col-form-label">Description</label>
                        <div className="col-sm-10">
                            <textarea name="description"
                                      value={this.state.data.description}
                                      id="game-day-description"
                                      className="form-control"
                                      onChange={this.handleInputChange}
                                      readOnly={this.props.mode === 'view'}
                            />
                        </div>
                    </div>
                    <ul>
                        {this.state.data.matches
                            .sort((a, b) => a.order > b.order ? 1 : (a.order === b.order ? 0 : -1))
                            .map((match, index) => <li key={index}>
                                <Match
                                    prefix={"matches." + index}
                                    data={match}
                                    handleInputChange={this.handleInputChange}
                                    handleRemoveMatch={this.handleRemoveMatch}
                                    handleRemovePlayer={this.handleRemovePlayer}
                                    disableRemoveButton={this.state.data.matches.length === 1}
                                    mode={this.props.mode}
                                    players={this.state.players}
                                />
                            </li>)}
                    </ul>

                    <Button
                        mode={this.props.mode}
                        type="button"
                        className="btn btn-primary"
                        onClick={(e) => {
                            e.preventDefault();
                            this.handleAddMatch();
                        }}
                        text="Add Another Match"
                    />

                    <Button
                        mode={this.props.mode}
                        type="submit"
                        className="btn btn-success"
                        onClick={this.handleSubmit}
                        text={title + " Game Day"}
                    />
                </form>
            </main>
        );
    }

    fetchGameDay() {
        Promise.all([
            this.fetchAsmGameDay(),
            this.fetchUserDataGameDay(),
        ]).then(([asmGameDay, userDataGameDay]) => {
            asmGameDay.matches.forEach(match => {
                const playersWithStatistics = match.playerStatistics.map(playerStatistics => playerStatistics.playerId);
                const playersWithoutStatistics = this.state.players.map(player => player.id).filter(playerId => !playersWithStatistics.includes(playerId));
                playersWithoutStatistics.forEach(playerId => match.playerStatistics.push(this.createEmptyPlayer(playerId)))
            })
            this.setState({data: {...asmGameDay, ...userDataGameDay}});
        });
    }

    fetchAsmGameDay() {
        return fetch(asmGameDaysHref + "/" + this.props.gameDayId, {
            method: 'GET',
            headers: {"Accept": "application/hal+json"},
            mode: "cors"
        })
            .then(response => response.json());
    }

    fetchUserDataGameDay() {
        return fetch(userDataGameDaysHref + "/" + this.props.gameDayId, {
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

    fetchUserDataPlayers(amateurSoccerGroupId) {
        function map(players) {
            return (players || []).map(player => ({
                id: extractId(player._links.player.href) || '',
                userId: player.userId || '',
                nickname: player.nickname || '',
            }));
        }

        return fetch(userDataPlayersDaysHref + "/search/byAmateurSoccerGroupId?amateurSoccerGroupId=" + amateurSoccerGroupId, {
            method: 'GET',
            headers: {"Accept": "application/hal+json"},
            mode: "cors"
        })
            .then(response => response.json())
            .then(json => json._embedded || {players: []})
            .then(embedded => map(embedded.players));
    }

    handleInputChange = (event) => {
        const target = event.target;
        const name = "data." + target.name;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        let currentState = {...this.state};
        setNestedKey(currentState, name.split('.'), value);
        this.setState(currentState);
    }

    handleAddMatch = () => {
        let currentState = {...this.state};
        const path = ("data.matches").split('.');
        let value = getNestedValue(currentState, path);
        value.push(this.createEmptyMatch(value.length + 1, this.state.players));
        setNestedKey(currentState, path, value);
        this.setState(currentState);
    }

    handleRemoveMatch = (order) => {
        let currentState = {...this.state};
        const value = this.state.data.matches.filter(value => value.order !== order);
        value.map((match, index) => match.order = index + 1)
        currentState.data.matches = value
        this.setState({currentState});
    }

    handleRemovePlayer = (key) => {
        let currentState = {...this.state};
        const path = ("data." + key).split('.');
        const playerToRemove = getNestedValue(currentState, path);
        let playerStatistics = path.slice(0, path.length - 2);
        playerStatistics.push("playerStatistics");
        let value = getNestedValue(currentState, playerStatistics)
            .filter(playerStatistic => playerToRemove.playerId !== playerStatistic.playerId);
        setNestedKey(currentState, playerStatistics, value);
        this.setState(currentState);
    }

    handleSubmit = (e) => {
        e.preventDefault();
        Promise.all([
            this.saveAsmGameDay(),
            this.saveUserDataGameDay(),
        ]).then(([asmGameDaySuccess, userDataGameDaySuccess]) => {
            if (asmGameDaySuccess && userDataGameDaySuccess) {
                this.setState({redirect: true})
            }
        });
    }

    saveAsmGameDay() {
        const requestBody = {
            amateurSoccerGroupId: this.state.data.amateurSoccerGroupId,
            date: this.state.data.date,
            matches: this.state.data.matches.map(match => ({
                order: match.order,
                playerStatistics: match.playerStatistics.filter(playerStatistics => ['A', 'B'].includes(playerStatistics.team))
            })),
        }
        return fetch(asmGameDaysHref + "/" + this.props.gameDayId, {
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

    saveUserDataGameDay() {
        const requestBody = {
            quote: this.state.data.quote,
            author: this.state.data.author,
            description: this.state.data.description,
        }
        return fetch(userDataGameDaysHref + "/" + this.props.gameDayId, {
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

    createEmptyMatch(order, players) {
        return {
            order: order,
            playerStatistics: players.map(player => this.createEmptyPlayer(player.id)),
        };
    }

    createEmptyPlayer(playerId) {
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
}

function Match(props) {
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
                    disabled={props.disableRemoveButton}
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
                {props.data.playerStatistics.map((playerStatistic, index) =>
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

function Button(props) {
    if (props.mode === 'view') {
        return '';
    }
    return (
        <button
            type={props.type}
            className={props.className}
            onClick={props.onClick}
            disabled={props.disabled}
        >
            {props.text}
        </button>
    );
}