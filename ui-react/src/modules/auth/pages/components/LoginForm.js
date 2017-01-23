import React from 'react';
import {textFieldChange} from './../../../utils/CommonEventHandler';
import {urlstringify} from './../../../utils/UtilFunx';
import axios from 'axios';

export default class LoginForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: 'jon_us',
            password: 'Password1'
        };

        this.request = axios.create({
            baseURL: process.env.REACT_APP_AUTH_END_POINT,
            auth: {
                username: process.env.REACT_APP_AUTH_CLIENT_ID,
                password: process.env.REACT_APP_AUTH_CLIENT_SECRET
            },
            headers: {
                'X-Requested-With': 'XMLHttpRequest-Extra'
            }

        });

        this.onChange = textFieldChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e) {
        e.preventDefault();
        let reqData = {
            client_secret: process.env.REACT_APP_AUTH_CLIENT_SECRET,
            client_id: process.env.REACT_APP_AUTH_CLIENT_ID,
            username: this.state.username,
            password: this.state.password,
            scope: 'read',
            grant_type: 'password'
        };
        this.request.post('/oauth/token', urlstringify(reqData)).then(function(response) {
            console.log('success', response);
        }).catch(function(error) {
            console.log(error);
        });
    }

    render() {
        return (
            <form>
                <div className="form-group">
                    <label htmlFor="username" className="control-label">Username</label>
                    <input onChange={this.onChange} value={this.state.username}
                      autoComplete="false" name="username" type="text"
                      className="form-control"/>
                </div>
                <div className="form-group">
                    <label htmlFor="password" className="control-label">Password</label>
                    <input onChange={this.onChange} value={this.state.password}
                      autoComplete="false" name="password" type="password"
                      className="form-control"/>
                </div>
                <div className="form-group">
                    <button onClick={this.onSubmit}
                      className="btn btn-primary btn-lg">Login</button>
                </div>
            </form>
        );
    }
}
