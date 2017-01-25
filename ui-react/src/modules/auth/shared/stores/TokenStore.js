const TOKEN_KEY = "_tk";

export default class TokenStore {

  constructor(storage) {
    this.storage = storage;
  }

  get() {
    return JSON.parse(this.storage.get(TOKEN_KEY));
  }

  set(token) {
    this.storage.set(TOKEN_KEY, JSON.stringify(token));
  }

  clear() {
    this.storage.remove(TOKEN_KEY);
  }
}
