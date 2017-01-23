import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {Router, browserHistory} from 'react-router';
import routes from './routes';
import {initAuth} from './modules/auth';
import {redux as reduxSamples} from './scratch/redux_samples';


reduxSamples();

initAuth({
    baseURL: process.env.REACT_APP_AUTH_END_POINT,
    auth: {
        username: process.env.REACT_APP_AUTH_CLIENT_ID,
        password: process.env.REACT_APP_AUTH_CLIENT_SECRET
    }
});
ReactDOM.render(
    <Router history={browserHistory} routes={routes}/>,
    document.getElementById('root'));
