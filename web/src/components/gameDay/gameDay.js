import React from "react";
import {v4 as uuidV4} from "uuid";
import {getNestedValue, setNestedKey} from "../../helper-functions";
import {ListGameDay} from "./listGameDay";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {extractId} from "../helper";

const titles = {'add': 'Create', 'edit': 'Edit', 'view': 'View'}

const asmGameDaysHref = "http://localhost:8080/gameDays";
const userDataGameDaysHref = "http://localhost:8081/gameDays";
const userDataPlayersDaysHref = "http://localhost:8081/players";

export class GameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: {
                amateurSoccerGroupId: this.props.amateurSoccerGroupId,
                date: '',
                quote: '',
                author: '',
                description: '',
                matches: [this.createEmptyMatch(1)],
            },
            players: [],
        }
    }

    componentDidMount() {
        this.fetchUserDataPlayers(this.props.amateurSoccerGroupId)
            .then(players => this.setState({players: players}))
        if (this.props.mode !== 'add') {
            this.fetchGameDay()
        }
    }

    render() {
        const title = titles[this.props.mode];
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
                <h1>
                    {title} Game Day | <a href="#" onClick={(e) => this.openGameDayList(e)}>List</a>
                </h1>
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
                                    handleAddPlayer={this.handleAddPlayer}
                                    handleRemovePlayer={this.handleRemovePlayer}
                                    disableRemoveButton={this.state.data.matches.length === 1}
                                    mode={this.props.mode}
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
            </div>
        );
    }

    fetchGameDay() {
        function findPlayer(playerId) {
            return this.state.players.filter(player => player.id === playerId)[0] || {nickname: '', userId: ''};
        }

        Promise.all([
            this.fetchAsmGameDay(),
            this.fetchUserDataGameDay(),
        ]).then(([asmGameDay, userDataGameDay]) => {
            asmGameDay.matches.forEach(match =>
                match.playerStatistics.forEach(playerStatistic => {
                    const player = findPlayer(playerStatistic.playerId);
                    console.log(player);
                    playerStatistic.nickname = player.nickname
                    playerStatistic.userId = player.userId
                }));
            this.setState({
                data: {...asmGameDay, ...userDataGameDay},
            });
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
        value.push(this.createEmptyMatch(value.length + 1));
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

    handleAddPlayer = (key) => {
        let currentState = {...this.state};
        const path = ("data." + key).split('.');
        let value = getNestedValue(currentState, path);
        value.push(this.createEmptyPlayer());
        setNestedKey(currentState, path, value);
        this.setState(currentState);
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
            this.saveUserDataPlayers()
        ]).then(([asmGameDaySuccess, userDataGameDaySuccess, userDataPlayers]) => {
            if (asmGameDaySuccess && userDataGameDaySuccess && userDataPlayers) {
                this.updateAppContent();
            }
        });
    }

    saveAsmGameDay() {
        const requestBody = {
            amateurSoccerGroupId: this.state.data.amateurSoccerGroupId,
            date: this.state.data.date,
            matches: this.state.data.matches,
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

    saveUserDataPlayers() {
        let requests = [];
        this.state.data.matches.forEach(match =>
            match.playerStatistics.forEach(playerStatistic => {
                const requestBody = {
                    amateurSoccerGroupId: this.state.data.amateurSoccerGroupId,
                    userId: playerStatistic.userId,
                    nickname: playerStatistic.nickname
                }
                const request = fetch(userDataPlayersDaysHref + "/" + playerStatistic.playerId, {
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
                requests.push(request);
            }));
        Promise.all(requests).then(result => console.log(result));
    }

    openGameDayList(e) {
        e.preventDefault();
        this.updateAppContent();
    }

    updateAppContent() {
        this.props.updateAppContent(
            <ListGameDay
                amateurSoccerGroupId={this.state.data.amateurSoccerGroupId}
                updateAppContent={this.props.updateAppContent}
            />
        )
    }

    createEmptyMatch(order) {
        return {
            order: order,
            playerStatistics: [
                this.createEmptyPlayer(),
                this.createEmptyPlayer(),
            ]
        };
    }

    createEmptyPlayer() {
        return {
            playerId: uuidV4(),
            nickname: '',
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

class Match extends React.Component {
    render() {
        return (
            <div>
                <h2>
                    Match #{this.props.data.order}
                    <Button
                        mode={this.props.mode}
                        type="submit"
                        className="btn btn-primary"
                        onClick={(e) => {
                            e.preventDefault();
                            this.props.handleRemoveMatch(this.props.data.order);
                        }}
                        disabled={this.props.disableRemoveButton}
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
                    {this.props.data.playerStatistics.map((playerStatistic, index) =>
                        <PlayerStatistic
                            key={index}
                            index={index}
                            prefix={this.props.prefix + ".playerStatistics." + index}
                            data={playerStatistic}
                            handleInputChange={this.props.handleInputChange}
                            handleRemovePlayer={this.props.handleRemovePlayer}
                            mode={this.props.mode}
                            disableRemovePlayerButton={this.props.data.playerStatistics.length < 3}
                        />
                    )}
                    </tbody>
                </table>

                <Button
                    mode={this.props.mode}
                    type="button"
                    className="btn btn-primary"
                    onClick={(e) => {
                        e.preventDefault();
                        return this.props.handleAddPlayer(this.props.prefix + ".playerStatistics");
                    }}
                    text="Add Another Player"
                />
            </div>
        );
    }
}

class PlayerStatistic extends React.Component {
    render() {
        const prefix = this.props.prefix + ".";
        return (
            <tr key={this.props.index}>
                <td>
                    <PlayerNickname
                        prefix={prefix}
                        nickname={this.props.data.nickname}
                        handleInputChange={this.props.handleInputChange}
                        mode={this.props.mode}
                    />
                </td>
                <td className='col-3'>
                    <div className="form-check form-check-inline">
                        <label className="form-check-label">
                            <input className="form-check-input"
                                   type="radio"
                                   name={prefix + "team"}
                                   value="A"
                                   checked={this.props.data.team === 'A'}
                                   min="0"
                                   max="99"
                                   onChange={this.props.handleInputChange}
                                   readOnly={this.props.mode === 'view'}
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
                                   checked={this.props.data.team === 'B'}
                                   onChange={this.props.handleInputChange}
                                   readOnly={this.props.mode === 'view'}
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
                                   checked={this.props.data.team === ''}
                                   onChange={this.props.handleInputChange}
                                   readOnly={this.props.mode === 'view'}
                            />
                            N/A
                        </label>
                    </div>
                </td>
                <td className='col-1'>
                    <input name={prefix + "goalsInFavor"}
                           type="number"
                           value={this.props.data.goalsInFavor}
                           min="0"
                           max="99"
                           className="form-control"
                           onChange={this.props.handleInputChange}
                           readOnly={this.props.mode === 'view'}
                    />
                </td>
                <td className='col-1'>
                    <input name={prefix + "goalsAgainst"}
                           type="number"
                           value={this.props.data.goalsAgainst}
                           min="0"
                           max="99"
                           className="form-control"
                           onChange={this.props.handleInputChange}
                           readOnly={this.props.mode === 'view'}
                    />
                </td>
                <td className='col-1'>
                    <input name={prefix + "yellowCards"}
                           type="number"
                           value={this.props.data.yellowCards}
                           min="0"
                           max="99"
                           className="form-control"
                           onChange={this.props.handleInputChange}
                           readOnly={this.props.mode === 'view'}
                    />
                </td>
                <td className='col-1'>
                    <input name={prefix + "blueCards"}
                           type="number"
                           value={this.props.data.blueCards}
                           min="0"
                           max="99"
                           className="form-control"
                           onChange={this.props.handleInputChange}
                           readOnly={this.props.mode === 'view'}
                    />
                </td>
                <td className='col-1'>
                    <input name={prefix + "redCards"}
                           type="number"
                           value={this.props.data.redCards}
                           min="0"
                           max="99"
                           className="form-control"
                           onChange={this.props.handleInputChange}
                           readOnly={this.props.mode === 'view'}
                    />
                </td>
            </tr>
        );
    }
}

class PlayerNickname extends React.Component {
    render() {
        return (
            <input name={this.props.prefix + "nickname"}
                   value={this.props.nickname}
                   onChange={this.props.handleInputChange}
                   type="text"
                   className="form-control"
                   readOnly={this.props.mode === 'view'}
            />
        );
    }
}

class Button extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        if (this.props.mode === 'view') {
            return '';
        }
        return (
            <button
                type={this.props.type}
                className={this.props.className}
                onClick={this.props.onClick}
                disabled={this.props.disabled}
            >
                {this.props.text}
            </button>
        );
    }
}