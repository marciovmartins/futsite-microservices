import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {PlayerPage} from "./components/players/PlayerPage";
import {GameDayPage} from "./components/gameDay/gameDayPage";
import {GameDayList} from "./components/gameDay/gameDayList";
import App, {Home} from "./components/app";
import {PlayerList} from "./components/players/playerList";
import {Player} from "./components/players/player";
import {GameDay} from "./components/gameDay/gameDay";
import {StatisticsIndex} from "./components/statistics/statisticsIndex";
import {PlayerStatistics} from "./components/statistics/playerStatistics";

ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route index element={<Home/>}/>
                    <Route path="gameDays" element={<GameDayPage/>}>
                        <Route index element={<GameDayList/>}/>
                        <Route path="new" element={<GameDay mode='add'/>}/>
                        <Route path=":gameDayId" element={<GameDay mode='edit'/>}/>
                    </Route>
                    <Route path="/players" element={<PlayerPage/>}>
                        <Route index element={<PlayerList/>}/>
                        <Route path="new" element={<Player mode='add'/>}/>
                        <Route path=":playerId" element={<Player mode='edit'/>}/>
                    </Route>
                    <Route path="/statistics" element={<StatisticsIndex/>}>
                        <Route index element={<PlayerStatistics/>}/>
                        <Route path="players" element={<PlayerStatistics/>}/>
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>,
    document.getElementById('root')
);