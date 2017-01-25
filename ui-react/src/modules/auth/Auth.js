import TokenStore from './shared/stores/TokenStore';
import UserStore from './shared/stores/UserStore';


class Auth {
    constructor() {
        this.initialized = false;
    }

    isInitialized() {
        return this.initialized;
    }

    init(context) {
        context.setTokenStore(new TokenStore(context.storage))
        .setUserStore(new UserStore(context.storage));
        if (this.isInitialized()) {
            throw new Error('Auth should be initialized only once.');
        }
        this.initialized = true;

        // Check whether we have some creds in the storage

    }
}

export default new Auth();
