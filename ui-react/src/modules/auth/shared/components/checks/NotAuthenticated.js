import React from 'react';
import userService from 'modules/auth/shared/services/UserService';
import {excludeProps, enforceRootElement} from 'modules/utils/UtilFunx';



export default class NotAuthenticated extends React.Component {
  static contextTypes = {
      user: React.PropTypes.object
    };

    render() {
      let authenticated = userService.isAuthenticated();
      if (this.props.roles) {
        if (authenticated) {
          authenticated = userService.hasRole(this.props.roles.split(','));
        } else {
          return null;
        }
      }

      let propsToForward = excludeProps(['roles'], this.props);

      return !authenticated ? enforceRootElement(this.props.children, propsToForward) : null;
    }
}
