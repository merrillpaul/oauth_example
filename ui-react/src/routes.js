import React from 'react';
import {IndexRoute, Route} from 'react-router';
import App from './App';
import {HelloPage} from './modules/dashboard';
import IndexPage from './pages/IndexPage';
import {authRoutes} from './modules/auth';

export default (
    <Route path="/" component={App}>
        <IndexRoute component={IndexPage}/>
        <Route path="/hello" component={HelloPage}/>
        {authRoutes}
    </Route>
);
