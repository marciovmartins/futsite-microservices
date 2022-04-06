import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {PlayerPage} from "./components/players/PlayerPage";
import {GameDayEdit, GameDayNew, GameDayView} from "./components/gameDay/gameDaySave";
import {GameDayPage} from "./components/gameDay/gameDayPage";
import {GameDayList} from "./components/gameDay/gameDayList";
import App, {Home} from "./components/app";
import {PlayerList} from "./components/players/playerList";

ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route index element={<Home/>}/>
                    <Route path="gameDays" element={<GameDayPage/>}>
                        <Route index element={<GameDayList/>}/>
                        <Route path="new" element={<GameDayNew/>}/>
                        <Route path=":gameDayId/view" element={<GameDayView/>}/>
                        <Route path=":gameDayId/edit" element={<GameDayEdit/>}/>
                    </Route>
                    <Route path="/players" element={<PlayerPage/>}>
                        <Route index element={<PlayerList/>}/>
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>,
    document.getElementById('root')
);