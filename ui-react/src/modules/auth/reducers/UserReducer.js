import { UserConstants, TokenConstants } from './../constants';
import context from './../../shared/AppContext';

const initialState = {
  username: '',
  id: '',
  isLoading: false,
  error: [],
  isAuthenticated: false
}
const userReducer = (state = initialState, action) => {
  switch(action.type) {
    case UserConstants.CURRENT_USERNAME: {
      state = {...state, username: action.payload};
      context.userStore.setCurrentUserName(action.payload);
      break;
    }
    case `${TokenConstants.REQUEST_TOKEN}_PENDING`:
    {
      state = {...state,
        isAuthenticated: false
      };
      break;
    }
    case `${TokenConstants.REQUEST_TOKEN}_REJECTED`: {
      state = Object.assign({}, initialState);
      context.userStore.clearCurrentUser();
      break;
    }
    case `${TokenConstants.REQUEST_TOKEN}_FULFILLED`: {
      state = {...state, isAuthenticated: true};
      break;
    }

    case `${UserConstants.GET_USER_INFO}_PENDING`:
    {
      state = {...state,
        isAuthenticated: false,
        isLoading: true
      };
      break;
    }

    case `${UserConstants.GET_USER_INFO}_REJECTED`:
    {
      state = Object.assign({}, initialState);
      state = {...state, error: [action.payload.response.data]};
      break;
    }

    case `${UserConstants.GET_USER_INFO}_FULFILLED`:
    case `${TokenConstants.AUTHENTICATE_TOKEN}_FULFILLED`: // when already a token is saved and app is refreshed
    {
      state = {...state, isAuthenticated: true, isLoading: false,
        error: []
      };
      state = Object.assign(state, action.payload.data);
      break;
    }
    case `${TokenConstants.AUTHENTICATE_TOKEN}_REJECTED`:
    {
      // TODO ?? do we need to clean up the user info if we are now in
      // refreshToken request phase
      break;
    }
    default:
      break;
  }
  return state;
};

export default userReducer;
