import React from 'react';
import ReactDOM from 'react-dom';
import {v4 as uuidV4} from "uuid";
import {CreateGameDay} from "./components/gameDay";

ReactDOM.render(
    <CreateGameDay gameDayId={uuidV4()}/>,
    document.getElementById('root')
);