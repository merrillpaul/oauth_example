import context from './../../shared/AppContext';
import { TokenConstants } from './../constants';
import userService from './../shared/services/UserService';

class TokenActions {

  login(username, password) {
    context.dispatch({
      type: TokenConstants.REQUEST_TOKEN,
      payload: userService.login(username, password)
    });
  }

  refreshToken(refreshToken) {
    context.dispatch({
      type: TokenConstants.REFRESH_TOKEN,
      payload: userService.refreshToken(refreshToken)
    });
  }


  authenticate(token) {
    context.dispatch({
      type: TokenConstants.AUTHENTICATE_TOKEN,
      payload: userService.getUserInfo()
    });
  }

}

export default new TokenActions();
