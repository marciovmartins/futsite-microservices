import React from 'react';
import {Link, Outlet} from "react-router-dom";

export function GameDay() {
    return <main>
        <nav>
            <Link to="/gameDays/">List</Link> |{" "}
            <Link to="/gameDays/new">New</Link>
        </nav>
        <Outlet/>
    </main>;
}