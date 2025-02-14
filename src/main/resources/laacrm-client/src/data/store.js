import { configureStore } from "@reduxjs/toolkit";
import moduleReducer from "./slice/moduleSlice";
import authReducer from "./slice/authSlice";

export const store = configureStore({
    reducer: {
        module: moduleReducer,
        auth: authReducer,
    },
});

export default store;
