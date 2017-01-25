const USR_KEY = "_usr";

export default class UserStore {


  constructor(storage) {
    this.storage = storage;
  }

  getCurrentUserName() {
    return this.storage.get(`${USR_KEY}_name`);
  }

  get() {

  }

  set(usr) {

  }

  setCurrentUserName(username) {
    this.storage.set(`${USR_KEY}_name`, username);
  }

  clearCurrentUser() {
    this.storage.remove(`${USR_KEY}_name`)
    .then(()=> this.storage.remove(USR_KEY));
  }
}
