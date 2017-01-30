import React from 'react';
import {Route} from 'react-router';
import Module1MainPage from './pages/Module1MainPage';
import Sub1Page from './pages/Sub1Page.js';
import {AuthenticatedRoute} from 'RouteCommons';

export default (
    <AuthenticatedRoute path="module1" component={Module1MainPage}>
      <Route path="sub1" component={Sub1Page}/>
    </AuthenticatedRoute>
);
