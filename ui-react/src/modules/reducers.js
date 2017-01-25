import {reducers as authReducers} from './auth/reducers';
// any other reducers
// import {module1Reducers} from './module1/reducers';
let moduleReducers = Object.assign({}, authReducers/*, add any more reducers*/);
export default moduleReducers;
