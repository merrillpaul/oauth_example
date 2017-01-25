import context from './AuthContext';
import {LocalStorage} from './../shared/storage';
import TokenStore from './shared/stores/TokenStore';
import UserStore from './shared/stores/UserStore';


class Auth {
    constructor() {
        this.initialized = false;
    }

    isInitialized() {
        return this.initialized;
    }

    init(store) {
        let storage = new LocalStorage();
        context.setStorage(storage)
        .setStore(store)
        .setTokenStore(new TokenStore(storage))
        .setUserStore(new UserStore(storage));
        if (this.isInitialized()) {
            throw new Error('Auth should be initialized only once.');
        }
        this.initialized = true;

    }
}

export default new Auth();
