const tokenReducer = (state = {
    accessToken: '',
    refreshToken: '',
    expiryTime: -1
}, action) => {
    switch (action.type) {
        case 'AUTHENTICATE':
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
            default:
              break;
    }
    return state;
};
export default tokenReducer;
