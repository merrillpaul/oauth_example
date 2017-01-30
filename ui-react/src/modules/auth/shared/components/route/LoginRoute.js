import { Route } from 'react-router';
import context from 'modules/shared/AppContext';
import userService from 'modules/auth/shared/services/UserService';

export default class LoginRoute extends Route {
    static defaultProps = {
        onEnter(nextState, replace, callback) {
            let authenticated = userService.isAuthenticated();
            if (authenticated) {
                var router = context.getRouter();
                var homeRoute = router.getHomeRoute();
                var authenticatedHomeRoute = router.getAuthenticatedHomeRoute();
                var redirectTo = (authenticatedHomeRoute || {}).path || (homeRoute || {}).path || '/';
                replace(redirectTo);
            }
            callback();
        }
    };
}
