import Route from 'react-router';
import userService from 'modules/auth/shared/services/UserService';
import context from 'modules/shared/AppContext';

export default class AuthenticatedRoute extends Route {
  static defaultProps = {
    onEnter(nextState, replace, callback) {
      let authenticated = userService.isAuthenticated();
      if (!authenticated) {
        let router = context.getRouter();
        let homeRoute = router.getHomeRoute();
        let loginRoute = router.getLoginRoute();
        let redirectTo = (loginRoute || {}).path || (homeRoute || {}).path || '/';
        replace(redirectTo);
      }
      callback();
    }
  };
}
