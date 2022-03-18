import React from "react";
import {ListGameDay} from "./listGameDay";

export class ViewGameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            gameDay: {
                date: "",
                matches: []
            }
        }
    }

    componentDidMount() {
        this.fetchGameDay()
    }

    render() {
        return (
            <div>
                <h1>
                    View Game Day | <a href="#" onClick={(e) => this.openGameDayList(e)}>List</a>
                </h1>
                <form>
                    <div className="row mb-3">
                        <label htmlFor="game-day-date" className="col-sm-2 col-form-label">Date</label>
                        <div className="col-sm-10">
                            <input type="date"
                                   value={this.state.gameDay.date}
                                   className="form-control"
                                   readOnly
                            />
                        </div>
                    </div>
                    <ul>
                        {this.state.gameDay.matches.map((match, index) => <li key={index}>
                            <ViewMatch data={match}/>
                        </li>)}
                    </ul>
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
            .then(gameDay => this.setState({gameDay}))
        ;
    }

    openGameDayList(e) {
        e.preventDefault();
        this.updateAppContent();
    }

    updateAppContent() {
        this.props.updateAppContent(
            <ListGameDay
                amateurSoccerGroupId={this.props.amateurSoccerGroupId}
                updateAppContent={this.props.updateAppContent}
            />
        )
    }
}

class ViewMatch extends React.Component {
    render() {
        return (
            <div>
                <h2>Match #{this.props.data.order}</h2>
                <ol>
                    {this.props.data.players.map((player, index) =>
                        <li key={index}>
                            <ViewPlayer data={player}/>
                        </li>
                    )}
                </ol>
            </div>
        );
    }
}

class ViewPlayer extends React.Component {
    render() {
        return (
            <div>
                <div className="row mb-3">
                    <label htmlFor="userId" className="col-sm-2 col-form-label">User Id</label>
                    <div className="col-sm-10">
                        <input type="text"
                               className="form-control"
                               value={this.props.data.userId}
                               readOnly
                        />
                    </div>
                </div>
                <fieldset className="row mb-3">
                    <legend className="col-form-label col-sm-2 pt-0">Team</legend>
                    <div className="col-sm-10">
                        <div className="form-check form-check-inline">
                            <label className="form-check-label">
                                <input className="form-check-input"
                                       type="radio"
                                       value="A"
                                       checked={this.props.data.team === 'A'}
                                       readOnly
                                />
                                A
                            </label>
                        </div>
                        <div className="form-check form-check-inline">
                            <label className="form-check-label">
                                <input className="form-check-input"
                                       type="radio"
                                       value="B"
                                       checked={this.props.data.team === 'B'}
                                       readOnly
                                />
                                B
                            </label>
                        </div>
                    </div>
                </fieldset>
                <div className="row mb-3">
                    <label htmlFor="goalsInFavor" className="col-sm-2 col-form-label">Goals in favor</label>
                    <div className="col-sm-10">
                        <input type="number"
                               value={this.props.data.goalsInFavor}
                               className="form-control"
                               readOnly
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="goalsAgainst" className="col-sm-2 col-form-label">Goals against</label>
                    <div className="col-sm-10">
                        <input type="number"
                               value={this.props.data.goalsAgainst}
                               className="form-control"
                               readOnly
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="yellowCards" className="col-sm-2 col-form-label">Yellow cards</label>
                    <div className="col-sm-10">
                        <input type="number"
                               value={this.props.data.yellowCards}
                               className="form-control"
                               readOnly
                        />
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="blueCards" className="col-sm-2 col-form-label">Blue cards</label>
                    <div className="col-sm-10">
                        <input type="number"
                               value={this.props.data.blueCards}
                               className="form-control"
                               readOnly/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="redCards" className="col-sm-2 col-form-label">Red cards</label>
                    <div className="col-sm-10">
                        <input type="number"
                               value={this.props.data.redCards}
                               className="form-control"
                               readOnly/>
                    </div>
                </div>
            </div>
        );
    }
}