import { TokenConstants, UserConstants } from './../constants';
import context from './../../shared/AppContext';
import userService from './../shared/services/UserService';
const initialState = {
    accessToken: '',
    fetching: false,
    refreshToken: '',
    expiresIn: -1
};
const dispatch = (dispatcher) => {
    setTimeout(()=> {
      context.dispatch(dispatcher)
  }, 0);
};

const tokenReducer = (state = initialState, action) => {
    switch (action.type) {
        case TokenConstants.SET_TOKEN:
            {
                let tokenPayload = action.payload;
                state = {
                    ...state,
                        accessToken: tokenPayload.accessToken,
                        refreshToken: tokenPayload.refreshToken,
                        expiryTime: tokenPayload.expiryTime

                }
                break;
            }
        case `${TokenConstants.REQUEST_TOKEN}_PENDING`:
        {
          state = {...state,
            fetching: true
          };
          context.tokenStore.clear();
          break;
        }
        case `${TokenConstants.REQUEST_TOKEN}_FULFILLED`:
        {
          let tokenPayload = action.payload.data;
          state = {...state,
            fetching: false,
            accessToken: tokenPayload.access_token,
            refreshToken: tokenPayload.refresh_token,
            expiresIn: tokenPayload.expires_in
          };
          context.tokenStore.set({
            accessToken: tokenPayload.access_token,
            refreshToken: tokenPayload.refresh_token,
            expiresIn: tokenPayload.expires_in
          });
          dispatch({
            type: UserConstants.GET_USER_INFO,
            payload: userService.getUserInfo()
          });
          break;
        }
        case `${TokenConstants.REQUEST_TOKEN}_REJECTED`:
        {
          state = Object.assign({}, initialState);
          context.tokenStore.clear();
          break;
        }
        default:
          break;
    }
    return state;
};
export default tokenReducer;
