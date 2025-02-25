import {createSlice} from "@reduxjs/toolkit";

const moduleSlice = createSlice({
    name: 'module',
    initialState: {
        modules: []
    },
    reducers: {
        loadModules: (state, action) => {
            state.modules = action.payload;
        }
    }
});

export const { loadModules } = moduleSlice.actions;

export default moduleSlice.reducer;