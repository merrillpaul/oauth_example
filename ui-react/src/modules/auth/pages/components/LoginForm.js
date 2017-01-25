import React from 'react';
import {textFieldChange} from 'modules/utils/CommonEventHandler';
import TokenActions from 'modules/auth/actions/TokenActions';

export default class LoginForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: 'jon_us',
            password: 'Password1'
        };

        this.onChange = textFieldChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e) {
        e.preventDefault();
        TokenActions.login(this.state.username, this.state.password);
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
