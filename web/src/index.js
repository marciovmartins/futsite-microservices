import React from 'react';
import ReactDOM from 'react-dom';
import {ListGameDay} from "./components/gameDay/listGameDay";

class App extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            content: <ListGameDay
                amateurSoccerGroupId="926358df-c1b0-4a14-8fc3-6f937e5cfa04"
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