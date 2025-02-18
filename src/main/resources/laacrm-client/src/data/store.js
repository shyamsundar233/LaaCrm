import { configureStore } from "@reduxjs/toolkit";
import moduleReducer from "./slice/moduleSlice";
import recordReducer from "./slice/recordSlice";

export const store = configureStore({
    reducer: {
        module: moduleReducer,
        record: recordReducer
    },
});

export default store;
