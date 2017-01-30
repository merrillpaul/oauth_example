import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import { browserHistory } from 'react-router';
import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';
import promise from 'redux-promise-middleware';
import { Provider } from 'react-redux';

import Router from 'modules/auth/shared/components/route/Router';
import routes from 'routes';
import reducers from 'modules/reducers';
import {initAuth} from 'modules/auth';
import context from 'modules/shared/AppContext';
import {LocalStorage} from 'modules/shared/storage';

const composeEnhancers =
    process.env.NODE_ENV !== 'production' &&
    typeof window === 'object' &&
    window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ?
      window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({
        // Specify extension’s options like here name, actionsBlacklist, actionsCreators or immutablejs support
      }) : compose;

const _reducers = combineReducers(reducers);
const enhancer = composeEnhancers(
    applyMiddleware(promise(), thunk, logger())
  );
const store = createStore(_reducers, enhancer);
context.setStorage(new LocalStorage()).setStore(store);
initAuth(context, ()=> {
  ReactDOM.render(
    <Provider store={store}>
      <Router history={browserHistory} routes={routes}/>
    </Provider>  ,
      document.getElementById('root'));
});
