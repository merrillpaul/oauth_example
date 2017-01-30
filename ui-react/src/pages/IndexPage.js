import React from 'react';
import {Link} from 'react-router';
import {LogoutLink, NotAuthenticated} from 'RouteCommons';

export default class IndexPage extends React.Component {
    render() {
        return (
            <div className="container-fluid">
                <h2 className="text-center">Index Welcome!</h2>
                <hr />
                <div className="jumbotron">
                    <p>
                        <strong>To my React application!</strong>
                    </p>
                    <p>Ready to begin? </p>
                    <ol className="lead">
                        <NotAuthenticated>
                          <li><Link to="/login">Login</Link></li>
                        </NotAuthenticated>
                        <li><LogoutLink redirectTo="login"/></li>
                        <li><Link to="/profile">Custom Profile Data</Link></li>
                    </ol>
                </div>
            </div>
        );
    }
}
