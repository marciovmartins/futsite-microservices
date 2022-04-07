import React from "react";
import {Link, Outlet} from "react-router-dom";

export class PlayerPage extends React.Component {
    render() {
        return (
            <main>
                <nav>
                    <Link to='/players'>List</Link> |{' '}
                    <Link to='/players/new'>New</Link>
                </nav>
                <Outlet/>
            </main>
        )
    }
}