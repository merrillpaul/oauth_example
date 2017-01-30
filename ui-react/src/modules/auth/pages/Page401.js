import React from 'react';
import DocumentTitle from 'react-document-title';

export default class Page401 extends React.Component {
    render() {
        return (
          <DocumentTitle title='Unauthorized page'>
            <div className="container">
              <div className="alert alert-danger">
                <strong>Sorry!</strong> You do not have access to this page.
              </div>
            </div>
          </DocumentTitle>
        );
    }
}
