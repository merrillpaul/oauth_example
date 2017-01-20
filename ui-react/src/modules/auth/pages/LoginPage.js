import React from 'react';
import LoginForm from './components/LoginForm';
import DocumentTitle from 'react-document-title';

export default class LoginPage extends React.Component {
    render() {
        return (
          <DocumentTitle title='Login'>
            <div className="container">
              <div className="row">
                <div className="col-md-4 col-xs-12 col-md-offset-4">
                  <h3>
                    Login
                  </h3>
                  <hr/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-4 col-xs-12 col-md-offset-4">
                  <LoginForm/>
                </div>
              </div>
            </div>
          </DocumentTitle>
        );
    }
}
