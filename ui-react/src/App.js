import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import {Grid, Row, Col} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Header, Footer} from './layout';
import DocumentTitle from 'react-document-title';
import { connect } from 'react-redux';

class App extends Component {
    render() {
        return (
            <DocumentTitle title='OAuth Sample React App'>
                <div className="App">
                    <Grid>
                        <Row>
                            <Col lg={12} xs={12} md={3}>
                                <div className="App-header">
                                    <img src={logo} className="App-logo" alt="logo"/>
                                    <h2>Hello {this.props.user.username} - Welcome to React</h2>
                                </div>
                            </Col>
                        </Row>
                        <Row>
                            <Col lg={12} xs={12} md={3}>
                                <Header/>
                            </Col>
                        </Row>
                        <Row>
                            <Col sm={12} lg={12} xs={12} md={3}>
                                <div className="App-Content">
                                    {this.props.children}
                                </div>
                            </Col>
                        </Row>
                        <Row className="App-footer">
                            <Col lg={12} xs={12} md={3}>
                                <Footer/>
                            </Col>
                        </Row>
                    </Grid>
                </div>
            </DocumentTitle>
        );
    }
}

const mapStateToProps = (state) => {
  return {
    user: state.user
  }
};

export default connect(mapStateToProps)(App);
