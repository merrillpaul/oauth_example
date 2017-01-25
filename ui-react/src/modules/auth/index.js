import auth from './Auth';
export {default as authRoutes} from './routes';
export function initAuth(context) {
  auth.init(context);
};
