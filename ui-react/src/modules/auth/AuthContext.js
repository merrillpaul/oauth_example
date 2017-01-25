import UserService from './shared/services/UserService';

class AuthContext {
  constructor() {
    this.router = null;
    this.storage = null;
    this.userService = new UserService();
  }

  setRouter(router) {
    this.router = router;
    return this;
  }

  setStorage(storage) {
    this.storage = storage;
    return this;
  }

  setStore(store) {
    this.store = store;
    return this;
  }

  setTokenStore(tokenStore) {
    this.tokenStore = tokenStore;
    return this;
  }

  setUserStore(userStore) {
    this.userStore = userStore;
    return this;
  }

  dispatch(objOrDispatcherFunc) {
    this.store.dispatch(objOrDispatcherFunc);
  }

  getUserService() {
    return this.userService;
  }

  me() {
    return this.userService.getUserInfo();
  }
}

export default new AuthContext();
