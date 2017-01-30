import { Route } from 'react-router';
import userService from 'modules/auth/shared/services/UserService';
import context from 'modules/shared/AppContext';

export default class AuthenticatedRoute extends Route {
  static defaultProps = {
    onEnter(nextState, replace, callback) {
      let authenticated = userService.isAuthenticated();
      let authorized = false;
      if ( this.roles && authenticated) {
        authorized = userService.hasRole(this.roles.split(','));
      } else if (authenticated) {
        authorized = true;
      }

      let router = context.getRouter();
      let homeRoute = router.getHomeRoute();
      let loginRoute = router.getLoginRoute();
      if (!authenticated) {
        let redirectTo = (loginRoute || {}).path || (homeRoute || {}).path || '/';
        replace(redirectTo);
      } else if (authenticated && !authorized) {
        let unauthorizedRoute = router.getUnauthorizedRoute();
        let redirectTo = (unauthorizedRoute || {}).path || (homeRoute || {}).path || '/';
        replace(redirectTo);
      }
      callback();
    }
  };
}
