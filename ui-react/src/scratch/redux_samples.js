import { createStore, combineReducers, applyMiddleware } from 'redux';
import logger from 'redux-logger';
//import thunk from 'redux-thunk';
import reducers from './reducers'


const _reducers = combineReducers(reducers);
const middleWare = applyMiddleware(logger());
const store = createStore(_reducers, middleWare);

export function redux() {
  store.dispatch({type: 'USER_LOGGED_IN', payload: {
    name: 'Merrill',
    id: '100000'
  }});
}
