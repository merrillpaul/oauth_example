const userReducer = (state = {
  name: '',
  id: ''
}, action) => {
  if (action.type === 'USER_LOGGED_IN') {
    console.log('User logged in', action.payload);
  }
  return state;
};

export default userReducer;
