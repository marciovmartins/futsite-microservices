import React from 'react';
import ReactDOM from 'react-dom';

class CreateGameDay extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            date: null
        }
    }

    render() {
        return (
            <div>
                <h1>Create Game Day</h1>
                <form>
                    <div className="row mb-3">
                        <label htmlFor="game-day-date" className="col-sm-2 col-form-label">Date:</label>
                        <div className="col-sm-10">
                            <input name="date"
                                   type="date"
                                   placeholder="date"
                                   className="form-control"
                                   onChange={this.handleInputChange}/>
                        </div>
                    </div>
                    <AddMatch/>
                    <button className="btn btn-success">Add Game Day</button>
                </form>
            </div>
        );
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }
}

class AddMatch extends React.Component {
    render() {
        return (
            <div>
                <h2>Match</h2>
                <ol>
                    <li><AddPlayer index="0"/></li>
                    <li><AddPlayer index="1"/></li>
                </ol>
            </div>
        );
    }
}

class AddPlayer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: "",
            team: "",
            goalsInFavor: null,
            goalsAgainst: null,
            yellowCards: null,
            blueCards: null,
            redCards: null
        };
    }

    render() {
        const index = this.props.index
        return (
            <div>
                <div className="row mb-3">
                    <label htmlFor="userId" className="col-sm-2 col-form-label">User Id</label>
                    <div className="col-sm-10">
                        <input name={"player." + index + ".userId"}
                               type="text"
                               className="form-control"
                               value={this.state.userId}
                               onChange={this.handleInputChange}
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
                                       name={"player." + index + ".team"}
                                       value="A"
                                       onChange={this.handleInputChange}/>
                                A
                            </label>
                        </div>
                        <div className="form-check form-check-inline">
                            <label className="form-check-label">
                                <input className="form-check-input"
                                       type="radio"
                                       name={"player." + index + ".team"}
                                       value="B"
                                       onChange={this.handleInputChange}/>
                                B
                            </label>
                        </div>
                    </div>
                </fieldset>
                <div className="row mb-3">
                    <label htmlFor="goalsInFavor" className="col-sm-2 col-form-label">Goals in favor</label>
                    <div className="col-sm-10">
                        <input name={"player." + index + ".goalsInFavor"}
                               type="number"
                               className="form-control"
                               onChange={this.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="goalsAgainst" className="col-sm-2 col-form-label">Goals against</label>
                    <div className="col-sm-10">
                        <input name={"player." + index + ".goalsAgainst"}
                               type="number"
                               className="form-control"
                               onChange={this.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="yellowCards" className="col-sm-2 col-form-label">Yellow cards</label>
                    <div className="col-sm-10">
                        <input name={"player." + index + ".yellowCards"}
                               type="number"
                               className="form-control"
                               onChange={this.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="blueCards" className="col-sm-2 col-form-label">Blue cards</label>
                    <div className="col-sm-10">
                        <input name={"player." + index + ".blueCards"}
                               type="number"
                               className="form-control"
                               onChange={this.handleInputChange}/>
                    </div>
                </div>
                <div className="row mb-3">
                    <label htmlFor="redCards" className="col-sm-2 col-form-label">Red cards</label>
                    <div className="col-sm-10">
                        <input name={"player." + index + ".redCards"}
                               type="number"
                               className="form-control"
                               onChange={this.handleInputChange}/>
                    </div>
                </div>
            </div>
        );
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name.replace(/player\.\d+\./, '');
        this.setState({
            [name]: value
        });
    }
}

ReactDOM.render(
    <CreateGameDay/>,
    document.getElementById('root')
);