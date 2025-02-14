import { configureStore } from "@reduxjs/toolkit";
import moduleReducer from "./slice/moduleSlice";

export const store = configureStore({
    reducer: {
        module: moduleReducer
    },
});

export default store;
