export default class LocalStorage {
    constructor(type = 'local') {
        this.storage = type === 'local'
            ? localStorage
            : sessionStorage;
    }

    get(key) {
        return JSON.parse(this.storage.getItem(key));
    }

    set(key, value) {
        this.storage.setItem(key, JSON.stringify(value));
    }

    remove(key) {
        this.storage.removeItem(key);
    }
}
