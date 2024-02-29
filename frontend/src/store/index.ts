import {combineReducers, configureStore} from "@reduxjs/toolkit";
import { logger } from 'redux-logger';
import userReducer from "./user";
import apartmentsReducer from "./apartments";
import bookingsReducer from "./bookings";
import reviewsReducer from "./reviews";
import periodsReducer from "./periods";

const combinedReducer = combineReducers(
    {
        user: userReducer,
        apartments: apartmentsReducer,
        bookings: bookingsReducer,
        reviews: reviewsReducer,
        periods: periodsReducer,
    }
)

const rootReducer = (state : any, action : any) => {
  if (action.type === 'logout') {
    state = undefined;
  }
  return combinedReducer(state, action);
}
export const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat([logger]),
});

export type State = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

