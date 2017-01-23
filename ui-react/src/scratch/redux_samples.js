import {createStore, combineReducers, applyMiddleware} from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';
import promise from 'redux-promise-middleware';
import reducers from './reducers'
import axios from 'axios';

const _reducers = combineReducers(reducers);
const middleWare = applyMiddleware(promise(), thunk, logger());
const store = createStore(_reducers, middleWare);

export function redux() {
    store.dispatch({
        type: 'USER_LOGGED_IN',
        payload: {
            name: 'Merrill',
            id: '100000'
        }
    });

    store.dispatch((dispatch) => {
      setTimeout (()=> dispatch ({
        type: 'AUTHENTICATE',
        payload: {
          accessToken: '3424324234234',
          refreshToken: 'fsdgsdhasgsg',
          expiryTime: 100000
        }
      }), 5000);
    });

    store.dispatch((dispatch) => {
      dispatch({type: 'FETCH_USERS_START'});
      axios.get('http://rest.learncode.academy/api/wstern/users')
      .then((res) => {
        dispatch({type: 'RECEIVE_USERS', payload: res.data});
      }).catch((err) => {
        dispatch({type: 'FETCH_USERS_ERROR', payload: err});
      });
    });


    // via promises much cleaner, look at the console as it has some default
    // action types
    /*
    action @ 20:31:35.325 FETCH_USERS_VIA_PROMISES_PENDING
    core.js:111  prev state Object {user: Object, token: Object}
    core.js:115  action Object {type: "FETCH_USERS_VIA_PROMISES_PENDING"}
    core.js:123  next state Object {user: Object, token: Object}

    then after the xhr returns
    action @ 20:31:36.553 FETCH_USERS_VIA_PROMISES_FULFILLED
    core.js:111  prev state Object {user: Object, token: Object}
    core.js:115  action Object {type: "FETCH_USERS_VIA_PROMISES_FULFILLED", payload: Object}
    core.js:123  next state Object {user: Object, token: Object}

    if there is an error
    action @ 20:33:36.189 FETCH_USERS_VIA_PROMISES_REJECTED
    core.js:111  prev state Object {user: Object, token: Object}
    core.js:115  action Object {type: "FETCH_USERS_VIA_PROMISES_REJECTED", payload: Error: Network Error
        at createError (http://localhost:3000/static/js/bundle.js:61418:16)
        at …, error: true}
    core.js:123  next state Object {user: Object, token: Object}
    */
    store.dispatch({
      type: 'FETCH_USERS_VIA_PROMISES',
      payload: axios.get('http://rest.learncode.academy/api/wstern/users')
    });
}
