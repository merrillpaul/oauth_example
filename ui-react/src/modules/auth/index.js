import auth from './Auth';
export {default as authRoutes} from './routes';
export function initAuth(store) {
  auth.init(store);
};
