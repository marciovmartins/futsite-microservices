import {Link, Outlet} from "react-router-dom";
import React from "react";

export function StatisticsIndex() {
    return <main>
        <nav>
            <Link to="/statistics/players">Players</Link>
        </nav>
        <Outlet/>
    </main>;
}