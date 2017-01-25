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


  authenticate(token) {
    context.dispatch({
      type: TokenConstants.AUTHENTICATE_TOKEN,
      payload: userService.getUserInfo()
    });
  }

}

export default new TokenActions();
