import { UserConstants, TokenConstants } from './../constants';
import context from './../AuthContext';

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
    {
      state = {...state, isAuthenticated: true, isLoading: false,
        error: []
      };
      state = Object.assign(state, action.payload.data);
      break;
    }
    default:
      break;
  }
  return state;
};

export default userReducer;
