class AppContext {
  constructor() {
    this.router = null;
    this.storage = null;
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
    setTimeout(()=> {
          this.store.dispatch(objOrDispatcherFunc)
      }, 0);    
  }

}

export default new AppContext();
