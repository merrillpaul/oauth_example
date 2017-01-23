class Auth {
    constructor() {
        this.initialized = false;
    }

    isInitialized() {
        return this.initialized;
    }

    init(options) {
        options = options || {};
        console.log('Auth options', options);
        if (this.isInitialized()) {
            throw new Error('Auth should be initialized only once.');
        }
        this.initialized = true;
    }
}

export default new Auth();
