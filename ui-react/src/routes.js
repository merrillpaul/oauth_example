import React from 'react';
import {IndexRoute, Route} from 'react-router';
import App from './App';
import {HelloPage} from './pages/dashboard';
import IndexPage from './pages/IndexPage';

export default (
    <Route path="/" component={App}>
        <IndexRoute component={IndexPage}/>
        <Route path="/hello" component={HelloPage}/>
    </Route>
)