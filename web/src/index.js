import React from 'react';
import ReactDOM from 'react-dom';
import {v4 as uuidV4} from "uuid";

class CreateGameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            gameDayId: uuidV4(),
            amateurSoccerGroupId: uuidV4(),
            date: "",
            matches: [
                {
                    order: 1,
                    players: [
                        {
                            userId: uuidV4(),
                            team: "",
                            goalsInFavor: "",
                            goalsAgainst: "",
                            yellowCards: "",
                            blueCards: "",
                            redCards: ""
                        },
                        {
                            userId: uuidV4(),
                            team: "",
                            goalsInFavor: "",
                            goalsAgainst: "",
                            yellowCards: "",
                            blueCards: "",
                            redCards: ""
                        },
                    ]
                }
            ]
        }
    }

    render() {
        return (
            <div>
                <h1>Create Game Day</h1>
                <form>
                    <div className="row mb-3">
                        <label htmlFor="game-day-id" className="col-sm-2 col-form-label">Game Day Id</label>
                        <div className="col-sm-10">
                            <input name="gameDayId"
                                   type="text"
                                   value={this.state.gameDayId}
                                   className="form-control"
                                   readOnly/>
                        </div>
                    </div>
                    <div className="row mb-3">
                        <label htmlFor="game-day-id" className="col-sm-2 col-form-label">Amateur Soccer Group Id</label>
                        <div className="col-sm-10">
                            <input name="amateurSoccerGroupId"
                                   type="text"
                                   value={this.state.amateurSoccerGroupId}
                                   className="form-control"
                                   onChange={this.handleInputChange}/>
                        </div>
                    </div>
                    <div className="row mb-3">
                        <label htmlFor="game-day-date" className="col-sm-2 col-form-label">Date</label>
                        <div className="col-sm-10">
                            <input name="date"
                                   type="date"
                                   value={this.state.date}
                                   className="form-control"
                                   onChange={this.handleInputChange}/>
                        </div>
                    </div>
                    <ul>
                        {this.state.matches.map((match, index) => <li key={index}>
                            <AddMatch
                                prefix={"matches." + index}
                                data={match}
                                handleInputChange={this.handleInputChange}
                            />
                        </li>)}
                    </ul>
                    <button type="submit" className="btn btn-success" onClick={this.handleSubmit}>Add Game Day</button>
                </form>
            </div>
        );
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        let currentState = {...this.state};
        setNestedKey(currentState, name.split('.'), value);
        this.setState(currentState);
    }

    handleSubmit = (e) => {
        e.preventDefault();
        let requestBody = {
            amateurSoccerGroupId: this.state.amateurSoccerGroupId,
            date: this.state.date,
            matches: this.state.matches,
        }
        fetch('http://localhost:8080/gameDays/' + this.state.gameDayId, {
            method: 'PUT',
            headers: {
                "content-type": "application/hal+json",
                "crnk-compact": "true"
            },
            body: JSON.stringify(requestBody)
        }).then(function () {
            console.log('Sent!')
        })
    }
}

class AddMatch extends React.Component {
    render() {
        return (
            <div>
                <h2>Match #{this.props.data.order}</h2>
                <ol>
                    {this.props.data.players.map((player, index) =>
                        <li key={index}>
                            <AddPlayer
                                prefix={this.props.prefix + ".players." + index}
                                data={player}
                                handleInputChange={this.props.handleInputChange}
                            />
                        </li>
                    )}
                </ol>
            </div>
        );
    }
}

class AddPlayer extends React.Component {
    render() {
        const prefix = this.props.prefix + ".";
        return (
            <div>
                <div className="row mb-3">
                    <label htmlFor="userId" className="col-sm-2 col-form-label">User Id</label>
                    <div className="col-sm-10">
                        <input name={prefix + "userId"}
                               type="text"
                               className="form-control"
                               value={this.props.data.userId}
                               onChange={this.props.handleInputChange}
                               required/>
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
                                       onChange={this.props.handleInputChange}/>
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
                                       onChange={this.props.handleInputChange}/>
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
                               className="form-control"
                               onChange={this.props.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="goalsAgainst" className="col-sm-2 col-form-label">Goals against</label>
                    <div className="col-sm-10">
                        <input name={prefix + "goalsAgainst"}
                               type="number"
                               value={this.props.data.goalsAgainst}
                               className="form-control"
                               onChange={this.props.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="yellowCards" className="col-sm-2 col-form-label">Yellow cards</label>
                    <div className="col-sm-10">
                        <input name={prefix + "yellowCards"}
                               type="number"
                               value={this.props.data.yellowCards}
                               className="form-control"
                               onChange={this.props.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="blueCards" className="col-sm-2 col-form-label">Blue cards</label>
                    <div className="col-sm-10">
                        <input name={prefix + "blueCards"}
                               type="number"
                               value={this.props.data.blueCards}
                               className="form-control"
                               onChange={this.props.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="redCards" className="col-sm-2 col-form-label">Red cards</label>
                    <div className="col-sm-10">
                        <input name={prefix + "redCards"}
                               type="number"
                               value={this.props.data.redCards}
                               className="form-control"
                               onChange={this.props.handleInputChange}/>
                    </div>
                </div>
            </div>
        );
    }
}

/**
 * Sets a value of nested key string descriptor inside a Object.
 * It changes the passed object.
 * Ex:
 *    let obj = {a: {b:{c:'initial'}}}
 *    setNestedKey(obj, ['a', 'b', 'c'], 'changed-value')
 *    assert(obj === {a: {b:{c:'changed-value'}}})
 *
 * @param {object} obj   Object to set the nested key
 * @param {string[]} path  An array to describe the path(Ex: ['a', 'b', 'c'])
 * @param {object} value Any value
 */
export const setNestedKey = (obj, path, value) => {
    if (path.length === 1) {
        obj[path] = value
        return
    }
    return setNestedKey(obj[path[0]], path.slice(1), value)
}

ReactDOM.render(
    <CreateGameDay gameDayId={uuidV4()}/>,
    document.getElementById('root')
);