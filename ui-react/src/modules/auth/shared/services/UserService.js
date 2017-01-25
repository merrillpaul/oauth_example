import axios from 'axios';
import {urlstringify} from 'modules/utils/UtilFunx';
import context from 'modules/shared/AppContext';


class UserService {
    constructor() {
        this.request = axios.create({
            baseURL: process.env.REACT_APP_AUTH_END_POINT,
            headers: {
                'X-Requested-With': 'XMLHttpRequest-Extra'
            }

        });
    }

    login(username, password) {
      let reqData = {
          client_secret: process.env.REACT_APP_AUTH_CLIENT_SECRET,
          client_id: process.env.REACT_APP_AUTH_CLIENT_ID,
          username: username,
          password: password,
          scope: 'read',
          grant_type: 'password'
      };
      return this.request.post('/oauth/token', urlstringify(reqData), {
        auth: {
            username: process.env.REACT_APP_AUTH_CLIENT_ID,
            password: process.env.REACT_APP_AUTH_CLIENT_SECRET
        }
      });
    }

    refreshToken(refreshToken) {
      return Promise.resolve({success: true});
    }

    getUserInfo() {
      let token = context.tokenStore.get();
      return this.request.get('/user', {
        headers: {
          "Authorization": `Bearer ${token.accessToken}`
        }
      });
    }

    getCurrentToken() {
      return context.store.getState().token;
    }

    getCurrentUser() {
      return context.store.getState().user;
    }

    isAuthenticated() {
      return context.store.getState().user.isAuthenticated;
    }

}

export default new UserService();
