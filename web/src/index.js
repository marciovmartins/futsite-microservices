import React from 'react';
import ReactDOM from 'react-dom';
import {v4 as uuidV4} from "uuid";
import {ListGameDay} from "./components/gameDay/listGameDay";

class App extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            content: <ListGameDay
                amateurSoccerGroupId={uuidV4()}
                updateAppContent={(e) => this.updateAppContent(e)}
            />
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
    <React.StrictMode>
        <App/>
    </React.StrictMode>,
    document.getElementById('root')
);