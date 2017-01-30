import context from 'modules/shared/AppContext';
import { TokenConstants } from 'modules/auth/constants';
import userService from 'modules/auth/shared/services/UserService';

class TokenActions {

  login(username, password) {
    context.dispatch({
      type: TokenConstants.REQUEST_TOKEN,
      payload: userService.login(username, password)
    });
  }

  refreshToken() {
    context.dispatch({
      type: TokenConstants.REFRESH_TOKEN,
      payload: userService.refreshToken()
    });
  }


  authenticate(token, cb) {

    context.dispatch({
      type: `${TokenConstants.AUTHENTICATE_TOKEN}_PENDING`
    });

    userService.getUserInfo()
    .then( (res) => {
      context.dispatch({
        type: `${TokenConstants.AUTHENTICATE_TOKEN}_FULFILLED`,
        payload: res,
        callback: cb
      });
    })
    .catch( (err) => {
      context.dispatch({
        type: `${TokenConstants.AUTHENTICATE_TOKEN}_REJECTED`,
        payload: err,
        callback: cb
      });
    });

  }


  logout(cb) {
    // TODO call the actual logout and then the cb
    context.tokenStore.clear();
    cb();
  }



}

export default new TokenActions();
