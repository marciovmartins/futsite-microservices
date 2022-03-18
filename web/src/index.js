import React from 'react';
import ReactDOM from 'react-dom';
import {v4 as uuidV4} from "uuid";
import {CreateGameDay} from "./components/gameDay";

class App extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            content: <CreateGameDay gameDayId={uuidV4()} updateAppContent={(e) => this.updateAppContent(e)}/>
        }
    }

    render() {
        return (
            <div>{this.state.content}</div>
        )
    }

    updateAppContent(content) {
        this.setState({content})
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('root')
);