import React from "react";
import {v4 as uuidV4} from "uuid";
import {getNestedValue, setNestedKey} from "../../helper-functions";
import {ListGameDay} from "./listGameDay";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const titles = {'add': 'Create', 'edit': 'Edit', 'view': 'View'}

const asmGameDaysHref = "http://localhost:8080/gameDays";
const userDataGameDaysHref = "http://localhost:8081/gameDays";

export class GameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: {
                amateurSoccerGroupId: this.props.amateurSoccerGroupId,
                date: "",
                quote: "",
                author: "",
                description: "",
                matches: [this.createEmptyMatch(1)]
            }
        }
    }

    componentDidMount() {
        if (this.props.mode !== 'add') {
            this.fetchGameDay(this.props.gameDayId)
        }
    }

    render() {
        const title = titles[this.props.mode];

        let saveButton = '';
        if (this.props.mode !== 'view') {
            saveButton = <button type="submit" className="btn btn-success" onClick={this.handleSubmit}>
                {title} Game Day
            </button>
        }

        let addMatchButton = '';
        if (this.props.mode !== 'view') {
            addMatchButton = <button
                type="submit"
                className="btn btn-primary"
                onClick={(e) => {
                    e.preventDefault();
                    this.handleAddMatch();
                }}
            >
                Add Another Match
            </button>
        }

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
                    {addMatchButton}
                    {saveButton}
                </form>
            </div>
        );
    }

    fetchGameDay() {
        Promise.all([
            this.fetchAsmGameDay(),
            this.fetchUserDataGameDay(),
        ]).then(([asmGameDay, userDataGameDay]) => {
            this.setState({
                data: {...asmGameDay, ...userDataGameDay}
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
            .then(response => response.json());
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
            this.saveUserDataGameDay()
        ]).then(([asmGameDaySuccess, userDataGameDaySuccess]) => {
            if (asmGameDaySuccess && userDataGameDaySuccess) {
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
            team: "",
            goalsInFavor: "",
            goalsAgainst: "",
            yellowCards: "",
            blueCards: "",
            redCards: ""
        };
    }
}

class Match extends React.Component {
    render() {
        const title = titles[this.props.mode];

        let addPlayerButton = '';
        if (this.props.mode !== 'view') {
            addPlayerButton = <button
                type="submit"
                className="btn btn-primary"
                onClick={(e) => {
                    e.preventDefault();
                    return this.props.handleAddPlayer(this.props.prefix + ".playerStatistics");
                }}
            >
                {title} player
            </button>
        }

        let removeMatchButton = '';
        if (this.props.mode !== 'view') {
            removeMatchButton = <button
                type="submit"
                className="btn btn-primary"
                onClick={(e) => {
                    e.preventDefault();
                    this.props.handleRemoveMatch(this.props.data.order);
                }}
                disabled={this.props.disableRemoveButton}
            >
                Remove
            </button>
        }

        return (
            <div>
                <h2>Match #{this.props.data.order} {removeMatchButton}</h2>
                <ol>
                    {this.props.data.playerStatistics.map((playerStatistic, index) =>
                        <li key={index}>
                            <Player
                                prefix={this.props.prefix + ".playerStatistics." + index}
                                data={playerStatistic}
                                handleInputChange={this.props.handleInputChange}
                                handleRemovePlayer={this.props.handleRemovePlayer}
                                mode={this.props.mode}
                                disableRemovePlayerButton={this.props.data.playerStatistics.length < 3}
                            />
                        </li>
                    )}
                </ol>
                {addPlayerButton}
            </div>
        );
    }
}

class Player extends React.Component {
    render() {
        const prefix = this.props.prefix + ".";

        let removePlayerButton = '';
        if (this.props.mode !== 'view') {
            removePlayerButton = <button
                type="submit"
                className="btn btn-primary"
                onClick={(e) => {
                    e.preventDefault();
                    return this.props.handleRemovePlayer(this.props.prefix);
                }}
                disabled={this.props.disableRemovePlayerButton}
            >
                Remove
            </button>
        }

        return (
            <div>
                <div className="row mb-3">
                    <label htmlFor="playerId" className="col-sm-2 col-form-label">Player Id</label>
                    <div className="col-1 col-sm-8">
                        <input name={prefix + "playerId"}
                               type="text"
                               className="form-control"
                               value={this.props.data.playerId}
                               onChange={this.props.handleInputChange}
                               required
                               readOnly={this.props.mode === 'view'}
                        />
                    </div>
                    <div className="col-2 col-sm-2">
                        {removePlayerButton}
                    </div>
                </div>
                <fieldset className="row mb-3">
                    <legend className="col-form-label col-sm-2 pt-0">Team</legend>
                    <div className="col-sm-10">
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
                                       min="0"
                                       max="99"
                                       onChange={this.props.handleInputChange}
                                       readOnly={this.props.mode === 'view'}
                                />
                                B
                            </label>
                        </div>
                    </div>
                </fieldset>
                <div className="row mb-3">
                    <label htmlFor="goalsInFavor" className="col-sm-2 col-form-label">Goals in favor</label>
                    <div className="col-sm-10">
                        <input name={prefix + "goalsInFavor"}
                               type="number"
                               value={this.props.data.goalsInFavor}
                               min="0"
                               max="99"
                               className="form-control"
                               onChange={this.props.handleInputChange}
                               readOnly={this.props.mode === 'view'}
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="goalsAgainst" className="col-sm-2 col-form-label">Goals against</label>
                    <div className="col-sm-10">
                        <input name={prefix + "goalsAgainst"}
                               type="number"
                               value={this.props.data.goalsAgainst}
                               min="0"
                               max="99"
                               className="form-control"
                               onChange={this.props.handleInputChange}
                               readOnly={this.props.mode === 'view'}
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="yellowCards" className="col-sm-2 col-form-label">Yellow cards</label>
                    <div className="col-sm-10">
                        <input name={prefix + "yellowCards"}
                               type="number"
                               value={this.props.data.yellowCards}
                               min="0"
                               max="99"
                               className="form-control"
                               onChange={this.props.handleInputChange}
                               readOnly={this.props.mode === 'view'}
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="blueCards" className="col-sm-2 col-form-label">Blue cards</label>
                    <div className="col-sm-10">
                        <input name={prefix + "blueCards"}
                               type="number"
                               value={this.props.data.blueCards}
                               min="0"
                               max="99"
                               className="form-control"
                               onChange={this.props.handleInputChange}
                               readOnly={this.props.mode === 'view'}
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="redCards" className="col-sm-2 col-form-label">Red cards</label>
                    <div className="col-sm-10">
                        <input name={prefix + "redCards"}
                               type="number"
                               value={this.props.data.redCards}
                               min="0"
                               max="99"
                               className="form-control"
                               onChange={this.props.handleInputChange}
                               readOnly={this.props.mode === 'view'}
                        />
                    </div>
                </div>
            </div>
        );
    }
}