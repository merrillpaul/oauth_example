import context from './../AuthContext';
import { TokenConstants } from './../constants';

class TokenActions {
  
  login(username, password) {

    context.dispatch({
      type: TokenConstants.REQUEST_TOKEN,
      payload: context.userService.login(username, password)
    });
  }

  refreshToken(refreshToken) {
    context.dispatch({
      type: TokenConstants.REFRESH_TOKEN,
      payload: context.userService.refreshToken(refreshToken)
    });
  }

}

export default new TokenActions();
