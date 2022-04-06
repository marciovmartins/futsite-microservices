import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {GameDayEdit, GameDayNew, GameDayView} from "./components/gameDay/gameDaySave";
import {GameDay} from "./components/gameDay/gameDay";
import {GameDayList} from "./components/gameDay/gameDayList";
import App, {Home} from "./components/app";

ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<App/>}>
                    <Route index element={<Home/>}/>
                    <Route path="gameDays" element={<GameDay/>}>
                        <Route index element={<GameDayList/>}/>
                        <Route path="new" element={<GameDayNew/>}/>
                        <Route path=":gameDayId/view" element={<GameDayView/>}/>
                        <Route path=":gameDayId/edit" element={<GameDayEdit/>}/>
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    </React.StrictMode>,
    document.getElementById('root')
);