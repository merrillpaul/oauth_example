import React from 'react';
import { Route } from 'react-router';
import {LoginRoute, UnauthorizedRoute} from 'RouteCommons';
import LoginPage from './pages/LoginPage';
import Page401 from './pages/Page401';


export default (
  <Route>
    <LoginRoute path="/login" component={LoginPage} />
    <UnauthorizedRoute path="/unauth" component={Page401} />
  </Route>
);
