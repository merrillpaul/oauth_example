import TokenStore from './shared/stores/TokenStore';
import UserStore from './shared/stores/UserStore';
import TokenActions from './actions/TokenActions';


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

        // if token already persisted
        let token = context.tokenStore.get();
        if (token) {
          TokenActions.authenticate(token.accessToken);
        }

    }
}

export default new Auth();
