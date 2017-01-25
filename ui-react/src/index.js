import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {Router, browserHistory} from 'react-router';
import routes from './routes';
import {createStore, combineReducers, applyMiddleware} from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';
import promise from 'redux-promise-middleware';
import { Provider } from 'react-redux';

import reducers from './modules/reducers';
import {initAuth} from './modules/auth';
import context from './modules/shared/AppContext';
import {LocalStorage} from './modules/shared/storage';


const _reducers = combineReducers(reducers);
const middleWare = applyMiddleware(promise(), thunk, logger());
const store = createStore(_reducers, middleWare);
context.setStorage(new LocalStorage()).setStore(store);
initAuth(context);

ReactDOM.render(
  <Provider store={store}>
    <Router history={browserHistory} routes={routes}/>
  </Provider>  ,
    document.getElementById('root'));
