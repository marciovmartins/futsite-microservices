import React from "react";
import {v4 as uuidV4} from "uuid";
import {GameDay} from "./gameDay";
import {ViewGameDay} from "./viewGameDay";

export class ListGameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            amateurSoccerGroupId: this.props.amateurSoccerGroupId,
            gameDays: []
        }
    }

    componentDidMount() {
        this.fetchGameDays(this.state.amateurSoccerGroupId)
    }

    render() {
        let addGameDayHref = 'http://localhost:8080/gameDays/' + uuidV4()
        return (
            <div>
                <h1>
                    List Game Days | <a href="#" onClick={(e) => this.openCreateGameDay(e, addGameDayHref)}>Add</a>
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
                               onClick={(e) => this.viewGameDay(e, gameDay._links.self.href)}
                            >{gameDay.date}</a>

                            &nbsp;
                            <a href={gameDay._links.self.href}
                               onClick={(e) => this.openEditGameDay(e, gameDay._links.self.href)}
                            >[edit]</a>

                            &nbsp;
                            <a href={gameDay._links.self.href}
                               onClick={(e) => this.removeGameDay(e, gameDay._links.self.href)}
                            >[remove]</a>
                        </li>
                    )}
                </ul>
            </div>
        );
    }

    fetchGameDays(amateurSoccerGroupId) {
        fetch('http://localhost:8080/gameDays/search/byAmateurSoccerGroupId?amateurSoccerGroupId=' + amateurSoccerGroupId, {
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

    openCreateGameDay(e, href) {
        e.preventDefault();
        this.props.updateAppContent(
            <GameDay
                href={href}
                amateurSoccerGroupId={this.state.amateurSoccerGroupId}
                updateAppContent={this.props.updateAppContent}
                mode="add"
            />
        )
    }

    openEditGameDay(e, href) {
        e.preventDefault();
        this.props.updateAppContent(
            <GameDay
                href={href}
                amateurSoccerGroupId={this.state.amateurSoccerGroupId}
                updateAppContent={this.props.updateAppContent}
                mode="edit"
            />
        )
    }

    removeGameDay(e, href) {
        e.preventDefault();
        fetch(href, {
            method: 'DELETE',
            mode: "cors"
        })
            .then(() => this.fetchGameDays(this.props.amateurSoccerGroupId));
    }

    viewGameDay(e, href) {
        e.preventDefault();
        this.props.updateAppContent(
            <ViewGameDay
                href={href}
                amateurSoccerGroupId={this.state.amateurSoccerGroupId}
                updateAppContent={this.props.updateAppContent}
            />
        )
    }
}