import React from 'react';
import {Link} from 'react-router';

export default class Module1MainPage extends React.Component {
    render() {
        return (
          <div className="row">
            <div className="col-xs-12 col-lg-4 col-offset-lg4">
              <div className="jumbotron row">
                Module 1 Main Page
              </div>
              <div className="row">
                <Link to="module1/sub1">Got to Sub1</Link>
              </div>
              <div className="row">
                {this.props.children}
              </div>
            </div>
          </div>
        );
    }
}
