import React from 'react';
import {IndexRoute} from 'react-router';
import App from 'App';
import {HelloPage} from 'modules/dashboard';
import IndexPage from 'pages/IndexPage';
import {AuthenticatedRoute, HomeRoute} from 'RouteCommons';

// routes
import {authRoutes} from 'modules/auth';
import {module1Routes} from 'modules/module1';

export default (
    <HomeRoute path="/" component={App}>
        <IndexRoute component={IndexPage}/>
        <AuthenticatedRoute roles="ROLE_ADMIN,ROLE_PRACTITIONER" path="/hello" component={HelloPage}/>
        {authRoutes}
        {module1Routes}
        <AuthenticatedRoute>
          <HomeRoute path='/home' component={IndexPage} />
        </AuthenticatedRoute>
    </HomeRoute>
);
