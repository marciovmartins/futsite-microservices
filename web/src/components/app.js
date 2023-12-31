import React from "react";
import {Link, Outlet} from "react-router-dom";
import {v4 as uuidV4} from "uuid";

export default function App() {
    return (
        <div>
            <nav>
                <Link to="/">Home</Link>&nbsp;|&nbsp;
                <Link to="/gameDays">Game Day</Link>&nbsp;|&nbsp;
                <Link to="/players">Players</Link>&nbsp;|&nbsp;
                <Link to="/statistics">Statistics</Link>
            </nav>
            <Outlet/>
        </div>
    )
}

export class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            amateurSoccerGroupId: this.getAmateurSoccerGroupId()
        }
    }

    render() {
        return (
            <main>
                <h1>Home</h1>
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
            </main>
        );
    }

    getAmateurSoccerGroupId() {
        let uuid = sessionStorage.getItem("amateurSoccerGroupId") || uuidV4();
        sessionStorage.setItem("amateurSoccerGroupId", uuid)
        return uuid;
    }

    handleAmateurSoccerGroupIdChange = event => {
        this.setState({amateurSoccerGroupId: event.target.value})
        sessionStorage.setItem("amateurSoccerGroupId", event.target.value)
    }
}