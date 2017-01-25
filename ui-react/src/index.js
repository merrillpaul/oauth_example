import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {Router, browserHistory} from 'react-router';
import routes from './routes';
import {createStore, combineReducers, applyMiddleware} from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';
import promise from 'redux-promise-middleware';
import reducers from './modules/reducers';
import {initAuth} from './modules/auth';

const _reducers = combineReducers(reducers);
const middleWare = applyMiddleware(promise(), thunk, logger());
const store = createStore(_reducers, middleWare);

initAuth(store);
ReactDOM.render(
    <Router history={browserHistory} routes={routes}/>,
    document.getElementById('root'));
