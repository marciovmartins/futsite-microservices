import React from "react";
import {v4 as uuidV4} from "uuid";
import {setNestedKey} from "../../helper-functions";
import {ListGameDay} from "./listGameDay";

export class GameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: {
                amateurSoccerGroupId: this.props.amateurSoccerGroupId || uuidV4(),
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
    }

    componentDidMount() {
        if (this.props.mode !== 'add') {
            this.fetchGameDay(this.props.href)
        }
    }

    render() {
        let titles = {'add': 'Create', 'edit': 'Edit'}
        let title = titles[this.props.mode];
        return (
            <div>
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
                                   className="form-control"
                                   onChange={this.handleInputChange}/>
                        </div>
                    </div>
                    <ul>
                        {this.state.data.matches.map((match, index) => <li key={index}>
                            <AddMatch
                                prefix={"matches." + index}
                                data={match}
                                handleInputChange={this.handleInputChange}
                            />
                        </li>)}
                    </ul>
                    <button type="submit" className="btn btn-success" onClick={this.handleSubmit}>{title} Game Day</button>
                </form>
            </div>
        );
    }

    fetchGameDay() {
        fetch(this.props.href, {
            method: 'GET',
            headers: {"Accept": "application/hal+json"},
            mode: "cors"
        })
            .then(response => response.json())
            .then(data => this.setState({data}))
        ;
    }


    handleInputChange = (event) => {
        const target = event.target;
        const name = "data." + target.name;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        let currentState = {...this.state};
        setNestedKey(currentState, name.split('.'), value);
        this.setState(currentState);
    }

    handleSubmit = (e) => {
        e.preventDefault();
        const requestBody = {
            amateurSoccerGroupId: this.state.data.amateurSoccerGroupId,
            date: this.state.data.date,
            matches: this.state.data.matches,
        }
        fetch(this.props.href, {
            method: 'PUT',
            headers: {"content-type": "application/hal+json"},
            body: JSON.stringify(requestBody)
        }).then(() => {
            this.updateAppContent();
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