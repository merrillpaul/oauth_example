import {createStore, combineReducers, applyMiddleware} from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';
import reducers from './reducers'
import axios from 'axios';

const _reducers = combineReducers(reducers);
const middleWare = applyMiddleware(thunk, logger());
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
}
