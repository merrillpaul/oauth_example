import { Route } from 'react-router';
import TokenActions from 'modules/auth/actions/TokenActions';
import context from 'modules/shared/AppContext';

export default class LogoutRoute extends Route {
    static defaultProps = {
        onEnter(nextState, replace, callback) {
            TokenActions.logout().then(() => {
                var router = context.getRouter();
                var homeRoute = router.getHomeRoute();
                var loginRoute = router.getLoginRoute();
                var redirectTo = this.redirectTo || (homeRoute || {}).path || (loginRoute || {}).path || '/';
                replace(redirectTo);
                callback();
            });
        }
    };
}
