import React from 'react';
import { Router as ReactRouter } from 'react-router';
import { keys } from 'lodash';

import context from 'modules/shared/AppContext';

//import userService from 'modules/auth/shared/services/UserService';
import HomeRoute from './HomeRoute';
import LoginRoute from './LoginRoute';
import LogoutRoute from './LogoutRoute';
import UnauthorizedRoute from './UnauthorizedRoute';
import AuthenticatedRoute from './AuthenticatedRoute';
import {deepForEach} from 'modules/utils/UtilFunx';

export default class Router extends ReactRouter {

    static defaultProps = ReactRouter.defaultProps;

    markedRoutes = {
        home: {
            type: HomeRoute,
            authenticated: {
                props: null
            },
            notAuthenticated: {
                props: null
            }
        },
        login: {
            type: LoginRoute,
            props: null
        },
        logout: {
            type: LogoutRoute,
            props: null
        },
        unauth: {
          type: UnauthorizedRoute,
          props: null
        }
    };

    constructor() {
        super(...arguments);
        if (this.props.routes) {
            // The reason we wrap in a div is because we just need to have a root element.
            this._mapMarkedRoutes(
                <div>{this.props.routes}</div>
            );
        } else {
            this._mapMarkedRoutes(this);
        }
        context.setRouter(this);
    }

    _mapMarkedRoutes(routes) {
        let markedRoutes = this.markedRoutes;

        deepForEach(routes, (node, parent) => {
            // Try and map the route node to a marked route.
            let routeKeys = keys(markedRoutes);
            let len = routeKeys.length
            let i = 0;
            for (; i< len; i++) {
              let routeName = routeKeys[i];
              let route = markedRoutes[routeName];
              if (node.type === route.type) {
                  let markedRoute = markedRoutes[routeName];

                  if (node.type === HomeRoute) {
                      if (parent.type === AuthenticatedRoute) {
                          markedRoute = markedRoute.authenticated;
                      } else {
                          markedRoute = markedRoute.notAuthenticated;
                      }
                  }
                  markedRoute.props = node.props;
                  break;
              }
            }
        });
    }

    getHomeRoute() {
        return this.markedRoutes.home.notAuthenticated.props;
    }

    getAuthenticatedHomeRoute() {
        return this.markedRoutes.home.authenticated.props;
    }

    getLoginRoute() {
        return this.markedRoutes.login.props;
    }

    getLogoutRoute() {
        return this.markedRoutes.logout.props;
    }

    getUnauthorizedRoute() {
        return this.markedRoutes.unauth.props;
    }


    postLogin() {
      var router = context.getRouter();
      var homeRoute = router.getHomeRoute();
      var authenticatedHomeRoute = router.getAuthenticatedHomeRoute();
      var redirectTo = (authenticatedHomeRoute || {}).path || (homeRoute || {}).path || '/';
      this.push(redirectTo);
    }
}
