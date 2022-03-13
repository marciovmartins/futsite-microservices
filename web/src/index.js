import React from 'react';
import ReactDOM from 'react-dom';

class CreateGameDay extends React.Component {
    render() {
        return (
            <div>
                <h1>Create Game Day</h1>
                <form>
                    <div className="mb-3 form-group">
                        <label htmlFor="game-day-date" className="form-label">Date:</label>
                        <input name="game-day-date" type="date" placeholder="date" className="form-control"/>
                    </div>
                    <AddMatch/>
                    <button className="btn btn-success me-md-2">Add Game Day</button>
                </form>
            </div>
        );
    }
}

class AddMatch extends React.Component {
    render() {
        return (
            <div>
                <h2>Add Match</h2>
                <AddPlayer/>
                <button className="btn btn-primary me-md-2">Add Match</button>
            </div>
        );
    }
}

class AddPlayer extends React.Component {
    render() {
        return (
            <div>
                <h3>Add Player</h3>
                <div className="mb-3 form-group">
                    <label htmlFor="userId" className="form-label">User Id:</label>
                    <input name="userId" type="text" className="form-control"/>
                </div>
                <div className="mb-3 form-group">
                    <label htmlFor="team" className="form-label">Team:</label>
                    <div className="mb-3 form-group">
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" name="team" id="teamA"/>
                            <label className="form-check-label" htmlFor="teamA">A</label>
                        </div>
                        <div className="form-check form-check-inline">
                            <input className="form-check-input" type="radio" name="team" id="teamB"/>
                            <label className="form-check-label" htmlFor="teamB">B</label>
                        </div>
                    </div>
                </div>
                <div className="row mb-3 form-group">
                    <label htmlFor="goalsInFavor" className="form-label">Goals in favor:</label>
                    <input name="goalsInFavor" type="number" className="form-control"/>
                </div>
                <div className="row mb-3 form-group">
                    <label htmlFor="goalsAgainst" className="form-label">Goals against:</label>
                    <input name="goalsAgainst" type="number" className="form-control"/>
                </div>
                <div className="row mb-3 form-group">
                    <label htmlFor="yellowCards" className="form-label">Yellow cards:</label>
                    <input name="yellowCards" type="number" className="form-control"/>
                </div>
                <div className="row mb-3 form-group">
                    <label htmlFor="blueCards" className="form-label">Blue cards:</label>
                    <input name="blueCards" type="number" className="form-control"/>
                </div>
                <div className="row mb-3 form-group">
                    <label htmlFor="redCards" className="form-label">Red cards:</label>
                    <input name="redCards" type="number" className="form-control"/>
                </div>
                <button className="btn btn-primary me-md-2">Add Player</button>
            </div>
        );
    }
}

ReactDOM.render(
    <CreateGameDay/>,
    document.getElementById('root')
);