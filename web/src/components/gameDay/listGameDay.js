import React from "react";
import {v4 as uuidV4} from "uuid";
import {GameDay} from "./gameDay";
import {extractId} from "../helper";

const asmGameDaysHref = 'http://localhost:8080/gameDays';

export class ListGameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            amateurSoccerGroupId: this.props.amateurSoccerGroupId || uuidV4(),
            gameDays: []
        }
    }

    componentDidMount() {
        this.fetchGameDays(this.state.amateurSoccerGroupId)
    }

    render() {
        return (
            <div>
                <h1>
                    List Game Days | <a href="#" onClick={(e) => this.openGameDay(e, uuidV4(), 'add')}>Add</a>
                </h1>
                <form>
                    <div className="row mb-3">
                        <label htmlFor="game-day-id" className="col-sm-2 col-form-label">Amateur Soccer Group Id</label>
                        <div className="col-sm-10">
                            <input name="amateurSoccerGroupId"
                                   type="text"
                                   value={this.state.amateurSoccerGroupId}
                                   className="form-control"
                                   onChange={this.handleAmateurSoccerGroupIdChange}/>
                        </div>
                    </div>
                </form>
                <ul>
                    {this.state.gameDays.map(gameDay =>
                        <li key={gameDay.date}>
                            <a href={gameDay._links.self.href}
                               onClick={(e) => this.openGameDay(e, extractId(gameDay._links.self.href), 'view')}
                            >{gameDay.date}</a>

                            &nbsp;
                            <a href={gameDay._links.self.href}
                               onClick={(e) => this.openGameDay(e, extractId(gameDay._links.self.href), 'edit')}
                            >[edit]</a>

                            &nbsp;
                            <a href={gameDay._links.self.href}
                               onClick={(e) => this.removeGameDay(e, extractId(gameDay._links.self.href))}
                            >[remove]</a>
                        </li>
                    )}
                </ul>
            </div>
        );
    }

    fetchGameDays(amateurSoccerGroupId) {
        fetch(asmGameDaysHref + '/search/byAmateurSoccerGroupId?amateurSoccerGroupId=' + amateurSoccerGroupId, {
            method: 'GET',
            headers: {"Accept": "application/hal+json"},
            mode: "cors"
        })
            .then(response => response.json())
            .then(data => data._embedded.gameDays)
            .then(gameDays => this.setState({gameDays}))
    }

    handleAmateurSoccerGroupIdChange = event => {
        this.setState({amateurSoccerGroupId: event.target.value})
        this.fetchGameDays(event.target.value);
    }

    openGameDay(e, gameDayId, mode) {
        e.preventDefault();
        this.props.updateAppContent(
            <GameDay
                gameDayId={gameDayId}
                amateurSoccerGroupId={this.state.amateurSoccerGroupId}
                updateAppContent={this.props.updateAppContent}
                mode={mode}
            />
        )
    }

    removeGameDay(e, gameDayId) {
        e.preventDefault();
        fetch(asmGameDaysHref + "/" + gameDayId, {
            method: 'DELETE',
            mode: "cors"
        })
            .then(() => this.fetchGameDays(this.props.amateurSoccerGroupId));
    }
}