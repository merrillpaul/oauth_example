const userReducer = (state = {
  name: '',
  id: ''
}, action) => {
  if (action.type === 'USER_LOGGED_IN') {
    state = {...state, name: action.payload.name, id: action.payload.id};
  }
  return state;
};

export default userReducer;
